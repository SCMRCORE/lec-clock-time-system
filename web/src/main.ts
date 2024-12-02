import { createApp } from 'vue'
import { createPinia } from 'pinia'
import { Message } from '@arco-design/web-vue'
import ArcoVue from '@arco-design/web-vue'
import ArcoVueIcon from '@arco-design/web-vue/es/icon'
import '@arco-design/web-vue/dist/arco.css'

import App from './App.vue'
import router from './router'
import './style.scss'

const app = createApp(App)
const pinia = createPinia()
Message._context = app._context

app.use(ArcoVue)
app.use(pinia)
app.use(router)
app.use(ArcoVueIcon)

app.mount('#app')
