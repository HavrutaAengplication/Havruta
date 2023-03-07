<template>
  <div>
    <h1>My Questions</h1>
    <div v-for="question in questions" :key="question.problemQuestion">
      {{ question.problemQuestion }}
      <button @click="OpenModifyPopUp"> MODIFY </button>
      <button @click="QDelete(question.id)"> DELETE </button>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import {BASE_URL, HEADERS} from "@/config";
export default {
  name:"MyProblems",
  components:{

  },
  created(){
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
      axios.get(`${BASE_URL}/mypage/problems`, {
        headers: HEADERS,
      }).then(response => {
        this.questions = response.data.problemList
      }).catch(error => {
        alert(error)
      });
    },
    QDelete(){
      let params = {}
      let headers = {"Authorization" : "temp"};
      axios.delete(`${BASE_URL}/mypage/problems/${this.problemId}`, {
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