<template>
  <div class="bg-gray" :class="{ active : popupview }">
    <div class="inside-popup">
      <pop-up @close-popup="popupGroup()"></pop-up>
      <h1> Create A Group </h1><br>
      <span>group name </span><input v-model="groupName"><br>
      <span><br>subject name </span><input v-model="subjectName">
      <button @click="CreateGroup()">Create</button>
    </div>
  </div>

  <div class="landing-page">
    <div class="upper-side">
      <div class="upper-left">
        <h3>Groups</h3>
        <ul>
          <li v-for="group in groups" :key="group.id">{{ group.name }}</li>
        </ul>
      </div>
      <div class="upper-right">
        <router-link to="/mypage">My Page</router-link>
      </div>
    </div>
    <div class="lower-side">
      <div class="lower-left">
        <h3>Search Groups</h3>
        <input type="text" v-model="searchTerm" placeholder="Search for groups..." />
        <ul>
          <li v-for="group in filteredGroups" :key="group.id">{{ group.name }}</li>
        </ul>
      </div>
      <div class="lower-right">
        <button @click="popupGroup">Create Group</button>
      </div>
    </div>
  </div>
</template>

<script>
import PopUp from '@/components/CreateGroup.vue'
//import http from "../http"

export default {
  components:{
    PopUp,
  },
  data() {
    return {
      popupview: false,
      groups: [
        { id: 1, name: '운영체제' },
        { id: 2, name: '알고리즘' },
        { id: 3, name: '네트워크' }
      ],
      searchTerm: ''
    };
  },
  computed: {
    filteredGroups() {
      return this.groups.filter(group => {
        return group.name.toLowerCase().includes(this.searchTerm.toLowerCase());
      });
    }
  },
  methods: {
    /*
    delete(id) {
      http
        .delete("/api/sample/sample/" + id)
        .then(response => {
          const { data } = response
          console.log(data)
        })
        .catch(error => {
          alert(error)
        })
    },
    create(id, params) {
      http
        .post("/api/sample/sample/", params)
        .then(response => {
          const { data } = response
          console.log(data)
        })
        .catch(error => {
          alert(error)
        })
      },
      update(id, params) {
        http
          .put("/api/sample/sample/" + id, {
            params: params,
          })
          .then(response => {
            const { data } = response
            console.log(data)
          })
          .catch(error => {
            alert(error)
          })
      },
    getGroup(){
      let params = {}
        http
          .get("/home", {
            params: params,
          })
          .then(response => {
            const { data } = response
            console.log(data)
            this.groupList = data.items
          })
          .catch(error => {
            alert(error)
          })
    },
    */
    popupGroup() {
      this.popupview = (this.popupview) ? false : true
      // code to create a new group
    },
    createGroup() {
      this.popupGroup();

    }
  },
  created(){
    this.getGroup()
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