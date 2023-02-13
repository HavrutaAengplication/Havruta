<template>
  <div> About Page </div>
  <button @click="get"> Get </button>
  <div class="about">
    <h1>{{ connectData }}</h1>
  </div>
</template>
<script>
export default {
  name: "About-aa",
  components: [],
  data() {
    return {
      connectData: "initial",
    };
  },
  methods: {
    get() {
      console.log('get start')
      this.axios.get("/home", {
        headers: {
          Authorization: "ABCDE",
        },
      }).then((response) => {
        this.connectData = response.data;
        console.log(this.connectData);
      });
      console.log('get end')
    },
  },
  mounted() {
    console.log('about mounted');
    // this.get();
  },
};
</script>