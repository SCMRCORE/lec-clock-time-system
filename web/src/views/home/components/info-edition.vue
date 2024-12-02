<template>
  <div class="profile">
    <div class="alert-box-item">
      <div class="bigImg-div" @click="toGetImg()">
        <img class="bigImg" :src="data.valueUrl" v-if="data.valueUrl" />
      </div>
    </div>

    <div class="name">
      <span v-if="!data.editingName" @dblclick="editName">
        {{ data.name || '请输入姓名' }}
      </span>
      <input v-else type="text" v-model="data.name" @blur="saveName" />
    </div>

    <div class="signature">
      <span v-if="!data.editingSignature" @dblclick="editSignature">
        {{ data.signature || '签名是一种态度，我想我可以更酷' }}
      </span>
      <input
        v-else
        type="text"
        v-model="data.signature"
        @blur="saveSignature"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { request } from '@/services/request'
import { Message } from '@arco-design/web-vue'
import axios from 'axios'
const data = reactive({
  name: '',
  signature: '',
  editingName: false,
  editingSignature: false,
  valueUrl: ''
})

const getUserInfo = async () => {
  const id = localStorage.getItem('id')
  const token = localStorage.getItem('token')
  const res = await request.get(`/api/user/info/${id}`, {
    headers: {
      token: token
    }
  })
  const { nickname, signature, avatar } = res.data
  data.name = nickname
  data.signature = signature
}

const editName = () => {
  data.editingName = true
}

const saveName = async () => {
  data.editingName = false
  const token = localStorage.getItem('token')
  await request.post(
    '/api/user/info/update',
    {
      nickname: data.name
    },
    {
      headers: {
        token: token
      }
    }
  )
  Message.success('操作成功')
}

const editSignature = () => {
  data.editingSignature = true
}

const saveSignature = () => {
  data.editingSignature = false
  const token = localStorage.getItem('token')
  axios
    .put(
      'http://58.87.105.56:8080/user/info/update',
      {
        signature: data.signature
      },
      {
        headers: {
          token: token
        }
      }
    )
    .then((response:any) => {
      alert('操作成功')
      console.log(response.data)
      // 可以在这里根据后端返回的数据更新前端的状态或做其他操作
    })
    .catch((error:any) => {
      console.error(error)
      // 可以在这里根据错误情况给出适当的提示或进行其他处理
    })
}

const toGetImg = () => {
  // inputElement = document.createElement('input')
  // inputElement.setAttribute('type', 'file')
  // inputElement.style.display = 'none'
  // inputElement.addEventListener('change', data.uploadFile, false)
  // document.body.appendChild(inputElement)
  // inputElement.click()
}
const uploadFile = (el: InputEvent) => {
  // if (el && el.target && el.target.files && el.target.files.length > 0) {
  //   const files = el.target.files[0]
  //   const isLt2M = files.size / 1024 / 1024 < 2
  //   if (!isLt2M) {
  //     data.$message.error('上传头像图片大小不能超过 2MB!')
  //   } else if (files.type.indexOf('image') === -1) {
  //     data.$message.error('请选择图片文件')
  //   } else {
  //     const reader = new FileReader()
  //     reader.readAsDataURL(el.target.files[0])
  //     reader.onload = () => {
  //       data.valueUrl = reader.result
  //       const formData = new FormData()
  //       formData.append('image', files)
  //       const token = localStorage.getItem('token') // 获取 token
  //       axios
  //         .put('http://58.87.105.56:8080/user/upload', formData, {
  //           headers: {
  //             token: token
  //           }
  //         })
  //         .then((response) => {
  //           alert('操作成功')
  //           console.log(response.data)
  //           // 可以在这里根据后端返回的数据更新前端的状态或做其他操作
  //         })
  //         .catch((error) => {
  //           console.error(error)
  //           // 可以在这里根据错误情况给出适当的提示或进行其他处理
  //         })
  //     }
  //   }
  // }
}
const beforeDestroy = () => {
  // if (inputElement) {
  //   inputElement.removeEventListener('change', data.uploadFile, false)
  //   document.body.removeChild(inputElement)
  //   console.log('inputelement destroy')
  // }
}

onMounted(() => {
  getUserInfo()
})
</script>

<style scoped lang="scss">
.alert-box-item {
  overflow: hidden;
}

.bigImg-div {
  margin: 0 auto;
  width: 5em;
  height: 5em;
  border-radius: 100%;
  overflow: hidden;
  border: 1px solid #ddd;
}

.bigImg {
  margin: 0 auto;
  display: block;
  width: 5em;
  height: 5em;
  border-radius: 50%;
}

.profile {
  width: 25em;
  margin: 5em 8em;
  border: 1px solid #ccc;
  padding: 1em;
}

.name,
.signature {
  color: black;
  margin: 2em;
}

.name span,
.name input {
  font-size: 2em;
}

.signature span,
.signature input {
  font-size: 1em;
}

input {
  width: 100%;
}
</style>
