module.exports = {
  lintOnSave: false,
  publicPath: process.env.NODE_ENV === 'development' ? './' : '././',
  outputDir: 'dist',
  devServer: {
    port: 8080,
    proxy: {
      '/': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        ws: true
      }
    }
  }
}