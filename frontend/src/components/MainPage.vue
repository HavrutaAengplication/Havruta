<template>
  <div>
    <loginBtn/>
  </div>
  <div class="landing-page">
    <div class="upper-side">
      <div class="upper-left">
        <button @click="getGroup">Get Data</button>
        <h3>Groups</h3>
        <button @click="showPopup">Add Group</button>
        <popup v-if="popupVisible" @close-popup="hidePopup" @send-group-name="addGroup"></popup>

        <br><br>
        <ul>
          <li v-for="group in filteredGroups" :key="group.groupId">{{ group.groupName }}</li>
        </ul>
        <input type="text" v-model="searchTerm" placeholder="Search for groups..." />

      </div>
      <div class="upper-right">
        <router-link to="/mypage">My Page</router-link>
      </div>
    </div>
  </div>
</template>

<script>
import Popup from '@/components/CreateGroup.vue'
import LoginBtn from '@/components/KaKaoLogin.vue'
import axios from 'axios'
import groupData from '@/assets/groupData.json'
import {BASE_URL} from "@/config";

export default {
  components:{
    Popup,
    LoginBtn,
  },
  data() {
    return {
      popupVisible: false,
      searchTerm: '',
      groupList: "",
      groupName: [],
      subjectName: "",
    };
  },
  computed: {
    filteredGroups() {
      return this.groupList;
      // return this.groupList.filter(group => {
      //   return group.groupName.toLowerCase().includes(this.searchTerm.toLowerCase());
      // });
    },
    testGroups() {
      return groupData.groups
    },
    headers() {
      return {
        "Authorization" : "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjc3NTU3NTc4LCJleHAiOjE2Nzc1OTM1Nzh9.41fGwW_LgMYOnSDkvGYt0JV9GOQOZC-uejooDgUEXFU",
        "Content-Type" : "application/json"
      };
    }
  },
  methods: {
    getGroup(){
      let params = {}
      let headers = {"Authorization" : "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjc3NTU3NTc4LCJleHAiOjE2Nzc1OTM1Nzh9.41fGwW_LgMYOnSDkvGYt0JV9GOQOZC-uejooDgUEXFU"};
      axios
          .get(`${BASE_URL}/home`, {
            params: params,
            headers: headers
          })
          .then(response => {
            const { data } = response
            console.log(data)
            console.log(data.groupList)
            this.groupList = data.groupList

          })
          .catch(error => {
            alert(error)
          })
    },

    showPopup() {
      this.popupVisible = true;
      this.freezeBody();
    },
    hidePopup() {
      this.popupVisible = false;
      this.unfreezeBody();
    },
    addGroup(name) {
      // const id = this.groups.length + 1;
      // this.groups.push({ id, name });
      this.hidePopup();
      console.log(name);
      axios
          .post(`${BASE_URL}/home/new`,
              {
              "groupName" : name
              },
              {
                headers: this.headers
              }
          )
          .then(response => {
            console.log(response)
            this.getGroup()
          })
          .catch(error => console.log(error))
    },
    freezeBody() {
      document.body.style.overflow = 'hidden';
    },
    unfreezeBody() {
      document.body.style.overflow = 'auto';
    },
  },
  mounted() {
    //  this.getGroup();
  },
};
</script>

<style>
.landing-page {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
}

.upper-side,
.lower-side {
  width: 100%;
  display: flex;
}

.lower-side {
  margin-top : 200px;
}

.upper-left,
.lower-left,
.upper-right,
.lower-right {

  width: 49%;
  padding: 2rem;
  box-sizing: border-box;
  align-items: center;
}

.upper-right {
  display: flex;
  flex-direction: column;
  align-items: center;
}

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