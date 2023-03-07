<template>
  <div>
    <h1>My Questions</h1>
    <div v-for="(question, index) in questions" :key="index">
      {{ question.problemQuestion }}
      <button @click="OpenModifyPopUp"> MODIFY </button>
      <button @click="QDelete()"> DELETE </button>
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
      problemID : 1,
    }
  },
  computed : {
    problemId() {
      return this.problemId
    }
  },
  methods: {
    QGet(){
      axios.get(`${BASE_URL}/mypage/problems`, {
        headers: HEADERS,
      }).then(response => {
        console.log(response.data)
        this.questions = response.data.myProblemDtoList
      }).catch(error => {
        alert(error)
      });
    },
    QDelete(){
      axios.delete(`${BASE_URL}/mypage/problems/` + this.problemID, {
        headers: HEADERS
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