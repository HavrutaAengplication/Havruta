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
      <p>groupName: {{group.name}}</p>
      <p>groupId: {{group.id}}</p>
    </div>
    <div class="box">
      <h2>Categories</h2>
      <ul>
        <li v-for="category in categories" :key="category.id">
          <router-link :to="'/groups/' + this.group.id + '/categories/' + category.id">{{category.name}}</router-link>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
import groupData from '@/groupData.json'

export default {
  name: "GroupPage",
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
  },
  methods: {
    initGroup() {
      let groupId = parseInt(this.$route.params.groupId);
      this.group = groupData.groups.find(group => group.id === groupId);
      this.isAdmin = true;
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
    this.initGroup()
  },
  mounted() {
    this.initGroup()
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