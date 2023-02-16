<template>
  <div class="bg-gray" :class="{ active : popupview }">
    <div class="inside-popup">
      <form v-on:submit="changeNickname">
        <h1>Change Nickname</h1>
        <input type="text" v-model="nickname">
        <button>submit</button>
      </form>
    </div>
  </div>

  <button @click="openPopup"> Change Nickname</button>
  <div>
    <p>My Groups</p>
    <ul>
      <li v-for="group in myGroups" :key="group.id">{{ group.name }}</li>
    </ul>
  </div>
  <div>
    <button @click="withdraw">Withdraw</button>
  </div>
  <div>
    <router-link to="/problems">My Problems</router-link>
  </div>
  <div>
    <router-link to="Home">Home</router-link>
  </div>
</template>

<script>
import axios from 'axios'
export default {
  name: "MyPage",
  data() {
    return {
      popupview : false,
      nickname: "",
      myGroups: [
        { id: 1, name: '운영체제' },
        { id: 2, name: '알고리즘' },
        { id: 3, name: '네트워크' },
      ],
    }
  },
  mounted() {
    // this.getMyGroupList();
  },
  methods: {
    openPopup() {
      this.popupview = true;
      console.log(this.popupview);
    },

    async changeNickname() {
      const response = await axios.put("http://localhost:8080/api/mypage");
      console.log(response);
      this.popupview = false;
    },

    async getMyGroupList() {
      const response = await axios.get("http://localhost:8080/api/mypage");
      console.log(response);
      this.myGroups = response.data;
    },

    async withdraw() {
      const params = "";
      const response = await axios.delete("http://localhost:8080/api/mypage", params);
      console.log(response);
    }
  }
}
</script>

<style scoped>
.bg-gray{
  opacity:0;
  position: absolute;
  display: none;
  visibility: hidden;
  text-align: center;
  width: 100%;
  height: 100%;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: lightgray;
}
.inside-popup{
  position: absolute;
  width: 80%;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 400px;
  height: 300px;
  background-color: white;
}

.active{
  opacity: 1;
  display: block;
  visibility: visible;
}
</style>