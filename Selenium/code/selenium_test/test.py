from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.common.exceptions import TimeoutException, NoSuchElementException
from selenium.webdriver.chrome.service import Service
from time import sleep
import os

# ── 初始化 ──────────────────────────────────────────────────
service = Service(r"d:\vscode_wenjian\python\python文件\selenium_test\chromedriver.exe")
driver = webdriver.Chrome(service=service)
driver.maximize_window()

BASE_URL = "http://localhost:80"
SCREENSHOT_DIR = os.path.join(os.path.dirname(__file__), "screenshots")
os.makedirs(SCREENSHOT_DIR, exist_ok=True)


# ── 工具函数 ────────────────────────────────────────────────
def screenshot(name: str):
    driver.save_screenshot(os.path.join(SCREENSHOT_DIR, f"{name}.png"))


def js_click(el):
    """用 JS 点击，绕过遮挡和动画"""
    driver.execute_script("arguments[0].scrollIntoView({block:'center'});", el)
    driver.execute_script("arguments[0].click();", el)


def wait_click(xpath: str, timeout: int = 15):
    """等待可点击后 JS 点击"""
    el = WebDriverWait(driver, timeout).until(
        EC.element_to_be_clickable((By.XPATH, xpath))
    )
    js_click(el)
    return el


def wait_visible(xpath: str, timeout: int = 15):
    return WebDriverWait(driver, timeout).until(
        EC.visibility_of_element_located((By.XPATH, xpath))
    )


def js_fill(xpath: str, value: str, timeout: int = 15):
    """
    等待输入框出现后，用 JS 设置 value 并触发 Vue 的 input 事件，
    确保 v-model 能感知到变化。
    """
    el = WebDriverWait(driver, timeout).until(
        EC.presence_of_element_located((By.XPATH, xpath))
    )
    driver.execute_script("arguments[0].scrollIntoView({block:'center'});", el)
    # 先清空再用 JS 赋值，触发 Vue input 事件
    driver.execute_script("""
        var el = arguments[0];
        var val = arguments[1];
        var nativeInputValueSetter = Object.getOwnPropertyDescriptor(
            window.HTMLInputElement.prototype, 'value').set;
        nativeInputValueSetter.call(el, val);
        el.dispatchEvent(new Event('input', { bubbles: true }));
        el.dispatchEvent(new Event('change', { bubbles: true }));
    """, el, value)
    return el


def dialog_fill(placeholder: str, value: str):
    """在 el-dialog__body 内填写输入框，用完整 XPath 定位"""
    xpath = (
        "//div[contains(@class,'el-dialog__body')]"
        f"//input[@placeholder='{placeholder}']"
    )
    js_fill(xpath, value)


def wait_dialog_body(timeout: int = 15):
    """等待对话框 body 出现且可见"""
    WebDriverWait(driver, timeout).until(
        EC.visibility_of_element_located((By.XPATH, "//div[contains(@class,'el-dialog__body')]"))
    )
    sleep(0.5)


def click_dialog_confirm():
    """点击对话框底部的主要确定按钮，直接用 JS 查找并点击"""
    # 先等按钮出现在 DOM 中
    WebDriverWait(driver, 15).until(
        EC.presence_of_element_located((
            By.XPATH,
            "//div[contains(@class,'el-dialog__footer')]//button[contains(@class,'el-button--primary')]"
        ))
    )
    sleep(0.3)
    # 用 JS 直接点击，完全绕过 Selenium 的可见/可点击检查
    driver.execute_script("""
        var btns = document.querySelectorAll('.el-dialog__footer .el-button--primary');
        for (var i = 0; i < btns.length; i++) {
            if (btns[i].offsetParent !== null) {
                btns[i].click();
                break;
            }
        }
    """)


# ── 登录 ────────────────────────────────────────────────────
def login():
    driver.get(BASE_URL)
    sleep(2)

    # 如果已经在首页（已登录），直接返回
    try:
        driver.find_element(By.XPATH, "//span[text()='系统管理']")
        print("[OK] 已登录，跳过登录步骤")
        return
    except NoSuchElementException:
        pass

    # 账号密码：先清空再输入，防止浏览器自动填充导致重复
    from selenium.webdriver.common.keys import Keys
    username_el = WebDriverWait(driver, 10).until(
        EC.presence_of_element_located((By.XPATH, "//input[@placeholder='账号']"))
    )
    username_el.click()
    username_el.send_keys(Keys.CONTROL + "a")
    username_el.send_keys(Keys.DELETE)

    password_el = driver.find_element(By.XPATH, "//input[@placeholder='密码']")
    password_el.click()
    password_el.send_keys(Keys.CONTROL + "a")
    password_el.send_keys(Keys.DELETE)

    screenshot("login_before")
    username_el.send_keys("admin")
    password_el.send_keys("admin123")

    # 验证码处理
    screenshot("login_captcha")
    try:
        captcha_img = driver.find_element(By.CLASS_NAME, "login-code-img")
        if captcha_img.is_displayed():
            print("\n[提示] 检测到验证码，请查看截图 screenshots/login_captcha.png")
            code = input("请输入验证码后按回车继续: ").strip()
            driver.find_element(By.XPATH, "//input[@placeholder='验证码']").send_keys(code)
    except NoSuchElementException:
        pass

    wait_click("//button[contains(@class,'el-button--primary') and .//span[contains(text(),'登')]]")
    sleep(3)
    wait_visible("//span[text()='系统管理']", timeout=20)
    screenshot("login_after")
    print("[OK] 登录成功")


# ── 导航 ────────────────────────────────────────────────────
def navigate_to(parent_menu: str, child_menu: str):
    parent = wait_visible(f"//span[text()='{parent_menu}']")
    parent_li = parent.find_element(By.XPATH, "./ancestor::li[1]")
    if "is-opened" not in (parent_li.get_attribute("class") or ""):
        js_click(parent)
        sleep(1)
    wait_click(f"//span[text()='{child_menu}']")
    sleep(2)


# ── 用户管理 ────────────────────────────────────────────────
def test_add_user():
    print("\n[TEST] 新增用户")
    login()
    navigate_to("系统管理", "用户管理")
    screenshot("add_user_before")

    wait_click("//div[contains(@class,'el-row')]//button[.//span[text()='新增']]")
    wait_dialog_body()

    # 用完整 XPath 在对话框内定位，避免与搜索栏同名输入框冲突
    dialog_fill("请输入用户昵称", "testUser")
    dialog_fill("请输入用户名称", "test001")
    dialog_fill("请输入用户密码", "Admin@123456")

    click_dialog_confirm()
    sleep(2)
    screenshot("add_user_after")
    print("[OK] 新增用户完成")


def test_edit_user():
    print("\n[TEST] 编辑用户")
    login()
    navigate_to("系统管理", "用户管理")
    screenshot("edit_user_before")

    wait_click("(//tbody//button[.//span[text()='修改']])[1]")
    wait_dialog_body()

    # 修改昵称
    xpath = "//div[contains(@class,'el-dialog__body')]//input[@placeholder='请输入用户昵称']"
    js_fill(xpath, "updatedUser")

    click_dialog_confirm()
    sleep(2)
    screenshot("edit_user_after")
    print("[OK] 编辑用户完成")


def test_delete_user():
    print("\n[TEST] 删除用户")
    login()
    navigate_to("系统管理", "用户管理")
    screenshot("delete_user_before")

    wait_click("(//tbody//button[.//span[text()='删除']])[1]")
    sleep(1)

    # MessageBox 确认
    wait_click("//div[contains(@class,'el-message-box')]//button[contains(@class,'el-button--primary')]")
    sleep(2)
    screenshot("delete_user_after")
    print("[OK] 删除用户完成")


# ── 角色管理 ────────────────────────────────────────────────
def test_role():
    print("\n[TEST] 新增角色")
    login()
    navigate_to("系统管理", "角色管理")
    screenshot("role_before")

    wait_click("//div[contains(@class,'el-row')]//button[.//span[text()='新增']]")
    wait_dialog_body()

    dialog_fill("请输入角色名称", "testRole")
    dialog_fill("请输入权限字符", "test_role")

    click_dialog_confirm()
    sleep(2)
    screenshot("role_after")
    print("[OK] 新增角色完成")

    # 删除刚新增的角色（搜索 testRole 后点行内删除）
    js_fill("//input[@placeholder='请输入角色名称']", "testRole")
    wait_click("//button[.//span[text()='搜索']]")
    sleep(1)
    wait_click("(//tbody//button[.//span[text()='删除']])[1]")
    sleep(0.5)
    driver.execute_script("""
        var btns = document.querySelectorAll('.el-message-box .el-button--primary');
        for (var i = 0; i < btns.length; i++) {
            if (btns[i].offsetParent !== null) { btns[i].click(); break; }
        }
    """)
    sleep(1)
    print("[OK] 删除测试角色完成")


# ── 菜单管理 ────────────────────────────────────────────────
def test_menu():
    print("\n[TEST] 新增菜单")
    login()
    navigate_to("系统管理", "菜单管理")
    screenshot("menu_before")

    wait_click("//div[contains(@class,'el-row')]//button[.//span[text()='新增']]")
    wait_dialog_body()

    # 菜单名称
    dialog_fill("请输入菜单名称", "testMenu")

    # 显示排序（el-input-number，用 JS 设置值并触发事件）
    driver.execute_script("""
        var inputs = document.querySelectorAll('.el-dialog__body .el-input-number input');
        if (inputs.length > 0) {
            var nativeInputValueSetter = Object.getOwnPropertyDescriptor(
                window.HTMLInputElement.prototype, 'value').set;
            nativeInputValueSetter.call(inputs[0], '1');
            inputs[0].dispatchEvent(new Event('input', { bubbles: true }));
            inputs[0].dispatchEvent(new Event('change', { bubbles: true }));
        }
    """)
    sleep(0.3)

    # 路由地址（目录类型必填）
    dialog_fill("请输入路由地址", "testmenu")

    click_dialog_confirm()
    sleep(2)
    screenshot("menu_after")
    print("[OK] 新增菜单完成")

    # 删除刚新增的菜单（搜索 testMenu 后点行内删除）
    js_fill("//input[@placeholder='请输入菜单名称']", "testMenu")
    wait_click("//button[.//span[text()='搜索']]")
    sleep(1)
    wait_click("(//tbody//button[.//span[text()='删除']])[1]")
    sleep(0.5)
    driver.execute_script("""
        var btns = document.querySelectorAll('.el-message-box .el-button--primary');
        for (var i = 0; i < btns.length; i++) {
            if (btns[i].offsetParent !== null) { btns[i].click(); break; }
        }
    """)
    sleep(1)
    print("[OK] 删除测试菜单完成")


# ── 通知公告 ────────────────────────────────────────────────
def test_notice():
    print("\n[TEST] 新增通知公告")
    login()
    navigate_to("系统管理", "通知公告")
    screenshot("notice_before")

    wait_click("//div[contains(@class,'el-row')]//button[.//span[text()='新增']]")
    wait_dialog_body()

    dialog_fill("请输入公告标题", "test notice")

    # 公告类型下拉（必填）：el-select 的 input 是 readonly，用 JS 点击触发下拉
    driver.execute_script("""
        var inputs = document.querySelectorAll('.el-dialog__body input[placeholder="请选择公告类型"]');
        if (inputs.length > 0) inputs[0].click();
    """)
    sleep(1)
    # 用 JS 直接点击第一个可见的下拉选项，不依赖 WebDriverWait
    driver.execute_script("""
        var items = document.querySelectorAll('.el-select-dropdown__list .el-select-dropdown__item');
        for (var i = 0; i < items.length; i++) {
            if (items[i].offsetParent !== null) {
                items[i].click();
                break;
            }
        }
    """)
    sleep(0.3)

    click_dialog_confirm()
    sleep(2)
    screenshot("notice_after")
    print("[OK] 新增通知公告完成")

    # 删除刚新增的通知公告（搜索 test notice 后点行内删除）
    js_fill("//input[@placeholder='请输入公告标题']", "test notice")
    wait_click("//button[.//span[text()='搜索']]")
    sleep(1)
    wait_click("(//tbody//button[.//span[text()='删除']])[1]")
    sleep(0.5)
    driver.execute_script("""
        var btns = document.querySelectorAll('.el-message-box .el-button--primary');
        for (var i = 0; i < btns.length; i++) {
            if (btns[i].offsetParent !== null) { btns[i].click(); break; }
        }
    """)
    sleep(1)
    print("[OK] 删除测试公告完成")


# ── 入口 ────────────────────────────────────────────────────
if __name__ == "__main__":
    try:
        test_add_user()
        test_edit_user()
        test_delete_user()
        test_role()
        test_menu()
        test_notice()
        print("\n[DONE] 所有测试完成")
    except Exception as e:
        screenshot("error")
        print(f"\n[ERROR] 测试异常: {e}")
        raise
    finally:
        sleep(2)
        driver.quit()
