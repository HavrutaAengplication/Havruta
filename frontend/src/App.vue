<template>
  <router-view/>
  <div>
    <img src="@/assets/logo.png">
  </div>
  <div>
    <HelloWorld/>

  </div>
</template>

<script>
  import HelloWorld from '@/components/HelloWorld.vue'
  export default {
    components: {
      HelloWorld
    },
    data() {},
    mounted() {
      this.load();
    },
    methods: {
      load() {
        this.axios.get('/home').then(res => {
          console.log("hihi");
          console.log('get home');
          console.log(res.data);
        });
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
