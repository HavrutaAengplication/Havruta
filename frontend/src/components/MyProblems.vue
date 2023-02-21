<template>
  <div>
    <h1>My Questions</h1>
    <div v-for="question in questions" :key="question.id">
      {{question.contents}}
      <button @click="OpenModifyPopUp"> MODIFY </button>
      <button @click="QDelete()">DELETE</button>
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
      axios.delete("http://localhost:8080/mypage/problems/" + problemID, {
        params: params,
        headers: headers
      }).then(response => {

      }).catch(error => {
        alert(error)
      })
    },
  },
  data() {
    return {
      questions : [
        {id: 1, contents: 'adsf'},
        {id: 2, contents: 'ssss'},
      ],
      problemID : 0,
    }
  }
}
</script>

<style>

</style>