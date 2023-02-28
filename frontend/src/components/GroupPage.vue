<template>
  <div class="parallel">
    <div class="box">
      <br>
    </div>
    <div class="box">
      <button v-if="isAdmin" @click="goToAdmin">Admin Settings</button>
    </div>
  </div>

  <div class="parallel">
    <div class="box">
      <p>groupName: {{groupName}}</p>
      <p>groupId: {{groupId}}</p>
    </div>
    <div class="box">
      <h2>Categories</h2>
      <ul>
        <li v-for="category in categories" :key="category.categoryId">
          <router-link :to="'/groups/' + this.group.id + '/categories/' + category.categoryId">
            {{category.categoryName}}</router-link>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import {BASE_URL, HEADERS} from "@/config";
export default {
  name: "GroupPage",
  data() {
    return {
      categories: [
        {
          categoryId: 1,
          categoryName: "C1",
          depth: 1,
        },
        {
          categoryId: 2,
          categoryName: "C2",
          depth: 2,
        },
        {
          categoryId: 3,
          categoryName: "C3",
          depth: 3,
        }
      ],
      groupName: "",
      isAdmin: false,
      isMember: false,
    }
  },
  computed: {
    groupId() {
      return this.$route.params.groupId
    }
  },
  methods: {
    getGroupData() {
      axios
          .get(`${BASE_URL}/groups/${this.groupId}`, {
            headers: HEADERS
          })
          .then(response => {
            console.log(response)
            this.groupName = response.groupName
            this.categories = response.categoryList
            this.isAdmin = response.isAdmin
            this.isMember = response.isMember
          })
          .catch(error => console.log(error))
    },
    goToAdmin() {
      console.log('GroupPage isAdmin: ' + this.isAdmin);
      this.$router.push({
        name: 'admin',
        query: { isAdmin: this.isAdmin },
      });
    }
  },
  created() {
  },
  mounted() {
    this.getGroupData()
  }

}
</script>

<style scoped>
.parallel {
  display: flex;
  flex-wrap: wrap;
}

.box {
  width: 50%;
}
</style>