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
      <CategoryView :model="this.categories" @get-group-data="getGroupData"></CategoryView>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import {BASE_URL, HEADERS} from "@/config";
import CategoryView from "@/components/CategoryView.vue";
export default {
  name: "GroupPage",
  components : {
    CategoryView,
  },
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
            this.groupName = response.data.groupName
            this.categories = response.data.categoryList
            this.isAdmin = response.data.isAdmin
            this.isMember = response.data.isMember
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