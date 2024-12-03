<template>
  <div class="info">
    <div class="avatar" @click="uploadAvatar">
      <a-avatar
          trigger-type="mask"
          :size="100"
      >
        <img
            alt="avatar"
            :src="selfUser.avatar || 'default-avatar-url'"
            style="cursor: pointer;"
        />

        <template #trigger-icon>
          <IconEdit />
          <input type="file" ref="fileInput" style="display: none;" @change="handleFile">
        </template>
      </a-avatar>
    </div>
    <div class="nickname">
      <a-typography-text editable v-model:editText="selfUser.nickname">{{ selfUser.nickname || '你可以试试更加炫酷的签名' }}</a-typography-text>
    </div>
    <div class="text">
      <a-typography-paragraph editable v-model:editText="selfUser.signature">{{ selfUser.signature }}</a-typography-paragraph>
    </div>
  </div>
</template>

<script setup lang="ts">
import { saveinfoService, uploadFileServices } from "@/services";
import { debounceAsync } from "@/services/debounce";
import { Message } from "@arco-design/web-vue";
import { ref, defineProps } from "vue"

const token = localStorage.getItem('token') || ""
const props = defineProps(['selfUser'])
const {selfUser} = props
const fileInput = ref(null)
const uploadAvatar = ()=>{
  if(fileInput.value){
    fileInput.value?.click()
  }
}
let id = 0;
const handleFile = (event: { target: { files: any[]; }; })=>{
  const file = event.target.files[0]
  const reader = new FileReader()
  reader.readAsDataURL(file)
  reader.onload = async()=>{
    selfUser.avatar = reader.result
    const res = await uploadFileServices(file,token)
    console.log(res);
    if(res.response?.code==200){
      Message.success("更改图片成功")
    }else{
      Message.error("出现错误啦")
    }
  }
}
const modifynickname = async (newNickname:string) => {
  if(id<=1){
    id++;
    return;
  }
  const res = await saveinfoService(newNickname,selfUser.signature,token)
  if(res.error?.response?.data.code==200){
    Message.success("修改成功")
  }
}
const modifysignature = async(newSignature:string) =>{
  if(id<=1){
    id++;
    return;
  }
  const res = await saveinfoService(selfUser.nickname,newSignature,token)
  if(res.error?.response?.data.code==200){
    Message.success("修改成功")
  }
}
watch(()=>selfUser.nickname, debounceAsync(modifynickname,3000))
watch(()=>selfUser.signature,debounceAsync(modifysignature,3000))


</script>

<style scoped lang="scss">
.info{
  display: flex;
  flex-direction: column;
  flex-wrap: wrap;
  align-items: center;
  width: 350px;
  height: 400px;
  background-color: rgb(250, 250, 250);
  border-radius: 30px;
  div{
    margin: 15px;
  }
}

</style>