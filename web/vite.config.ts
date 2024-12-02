import path from 'node:path'
import { fileURLToPath, URL } from 'node:url'
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueJsx from '@vitejs/plugin-vue-jsx'
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import { ArcoResolver } from 'unplugin-vue-components/resolvers'

const __dirname = fileURLToPath(new URL('.', import.meta.url))

export default defineConfig({
  plugins: [
    vue(),
    AutoImport({
      dts: path.resolve(__dirname, 'src/typings', 'auto-imports.d.ts'),
      imports: ['vue', 'vue-router', 'pinia', '@vueuse/core'],
      resolvers: [ArcoResolver()]
    }),
    Components({
      dirs: ['src/components', 'src/views/**/components'],
      dts: path.resolve(__dirname, 'src/typings', 'components.d.ts'),
      resolvers: [ArcoResolver({ sideEffect: true })]
    })
  ],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, 'src')
    }
  },
  server: {
    // host: '0.0.0.0', 需要局域网连接时启用
    port: 4000,
    proxy: {
      '/api': {
        target: 'http://58.87.105.56:8080',
        rewrite: (path: string) => path.replace(/^\/api/, ''),
        changeOrigin: true
      }
    }
  }
})
