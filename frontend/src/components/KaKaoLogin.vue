<template>
  <section class="test">
    <div v-on:click="kakaoLoginBtn">카카오 연동</div>
    <div>
      <!-- <button @click="loginBtn">TestButton</button> -->
    </div>
  </section>
</template>

<script>
import axios from 'axios'
import {BASE_URL} from "@/config";
export default {
  name: "KakaoLogin",
  data() {
    return {
      kakaoEmail : "",
    }
  },
  mounted() {
    window.Kakao.init('bcf53bc5ac6b74d28a8b6f659566e773')
    // Kakao Developers에서 요약 정보 -> JavaScript 키

  },
  methods: {
    kakaoLoginBtn:function(){
      const token = window.Kakao.Auth.getAccessToken();
      if (token) {
        window.Kakao.API.request({
          url: '/v1/user/unlink',
          success: function (response) {
            console.log(response)
            console.log(token);
          },
          fail: function (error) {
            console.log(error)
          },
        })
        window.Kakao.Auth.setAccessToken(undefined)
      }


      window.Kakao.Auth.login({
        success: function () {
          window.Kakao.API.request({
            url: '/v2/user/me',
            data: {
              property_keys: ["kakao_account.email"]
            },
            success: async function (response) {
              console.log(response);
              console.log(response.kakao_account);
              this.kakaoEmail = response.kakao_account.email;
            },
            fail: function (error) {
              console.log(error)
            },
          })
        },
        fail: function (error) {
          console.log(error)
        },
      })
      axios
          .get(`${BASE_URL}/users/login`, {
            "email" : this.kakaoEmail
          })
          .then(response => {
            console.log(response.userName);
            console.log(response.headers.token);
          })
    }
  }
}
</script>

<style scoped>
.test {
  display:flex;
  justify-content: center;
  align-items: center;
  height:100vh;
}
div {
  width: 200px;
  height:40px;
  background-color:#fdd101;
  color:white;
  display:flex;
  align-items: center;
  justify-content: center;
  cursor:pointer;
}
</style>