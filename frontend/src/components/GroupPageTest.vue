<template>
  <div>
    <p>groupName: {{group.name}}</p>
    <p>groupId: {{group.id}}</p>
  </div>
  <div>
    <h2>Categories</h2>
    <router-link to="`${categoryURL}`" v-for="category in categories" :key="category.id">
      {{ category.name }}<br/>
    </router-link>
  </div>
</template>

<script>
import groupData from '@/groupData.json'

export default {
  name: "GroupPageTest",
  data() {
    return {
      categories: [
        {
          id: 1,
          name: "C1",
          depth: 1,
        },
        {
          id: 2,
          name: "C2",
          depth: 2,
        },
        {
          id: 3,
          name: "C3",
          depth: 3,
        }
      ],
      group: {
        id: "",
        name: "",
      },
      isAdmin: false,
      isMember: false,
    }
  },
  computed: {
    categoryURL(category) {
      return ["http://localhost:8081", "groups", this.group.id, "categories", category.id].join("/");
    }
  },
  methods: {
    initGroup() {
      let groupId = parseInt(this.$route.params.groupId);
      this.group = groupData.groups.find(group => group.id === groupId);
    }
  },
  created() {
    this.initGroup()
  },

}
</script>

<style scoped>

</style>