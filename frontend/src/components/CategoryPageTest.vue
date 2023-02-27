<template>
  <div>
    <h1>{{this.url}}</h1>
  </div>
</template>

<script>
import axios from 'axios'
// import categoryData from '@/assets/categoryData.json'
export default {
  name: "CategoryPageTest",
  data() {
    return {
      problems:
      [

      ]
    }
  },
  computed: {
    groupId() { return this.$route.params.groupId; },
    categoryId() { return this.$route.params.categoryId; },
    url() {
      return ["http://localhost:8080", "groups", this.groupId, "category", this.categoryId].join('/')
    }
  },
  created() {
    this.getGroup();
  },
  methods: {
    getGroup(){
      let params = {}
      let headers = {"Authorization" : "temp"};
      axios
          .get(this.url, {
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
  }
}
</script>

<style scoped>

</style>