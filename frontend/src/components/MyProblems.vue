<template>
  <div>
    <h1>My Questions</h1>
    <div v-for="question in questions" :key="question.id">
      {{question.contents}}
      <button @click="OpenModifyPopUp"> MODIFY </button>
      <button @click="QDelete(question.id)"> DELETE </button>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
export default {
  name:"MyProblems",
  components:{

  },
  mounted(){
    this.QGet();
  },
  data() {
    return {
      questions : [],
      problemID : 0,
    }
  },
  methods: {
    QGet(){
      let params = {}
      let headers = {"Authorization" : "temp"};
      axios.get("http://localhost:8080/mypage/problems/", {
        params: params,
        headers: headers
      }).then(response => {
        const { data } = response
        this.questions = data.questions;
      }).catch(error => {
        alert(error)
      });
    },
    QDelete(){
      let params = {}
      let headers = {"Authorization" : "temp"};
      axios.delete("http://localhost:8080/mypage/problems/" + this.problemID, {
        params: params,
        headers: headers
      }).then(response => {
        console.log(response)
      }).catch(error => {
        alert(error)
      })
    },
  },
}
</script>

<style>

</style>