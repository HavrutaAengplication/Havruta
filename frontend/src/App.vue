<template>
  <div>
    <img src="@/assets/logo.png">
  </div>
  <div>
    <router-link to="/">Go to Home</router-link>
  </div>
  <div>
    <router-view/>
  </div>
  <button @click="load">Get Data</button>
</template>

<script>
import axios from 'axios'
export default {
  components: {
  },
  data() {
    return {
      res: "",
    }
  },
  mounted() {
    // this.load();
  },
  methods: {
    async load() {
      const response = await axios.get("http://localhost:8080/api/data");
      this.res = response;
    },

  }

}
</script>
<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

nav {
  padding: 30px;
}

nav a {
  font-weight: bold;
  color: #2c3e50;
}

nav a.router-link-exact-active {
  color: #42b983;
}
</style>
