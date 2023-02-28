<template>
  <div class="modal">
    <div class="overlay" @click="$emit('close-modal')"></div>
    <div class="modal-card">
      <h1>새로운 문제 생성</h1>
      <div>
        <label for="category">문제 카테고리 : </label>
        <select id="category" v-model="selectedCategory">
          <option v-for="category in categories" :key="category.id" :value="category.id">{{ category.name }}</option>
        </select>
      </div>
      <div>
        <label for="question-name">문제 이름 : </label>
        <input id="question-name" v-model="questionName">
      </div>
      <div>
        <label for="question-type">문제 타입 : </label>
        <select id="question-type" v-model="selectedQuestionType">
          <option value="multiple-choice">객관식</option>
          <option value="short-answer">단답형</option>
        </select>
      </div>
      <div>
        <label for="question-content">문제 내용 : </label>
        <textarea id="question-content" v-model="questionContent"></textarea>
      </div>
      <div v-if="selectedQuestionType === 'multiple-choice'">
        <div v-for="(choice, index) in choices" :key="index">
          <label>{{ index + 1 }}:</label>
          <input v-model="choice">
          <button @click="addChoice">Add Choice</button>
        </div>
      </div>
      <div v-if="selectedQuestionType === 'short-answer'">
        <div>
          <label for="short-answer-input">단답형 : </label>
          <input id="short-answer-input" v-model="shortAnswer">
        </div>
      </div>
      <button @click="submitQuestion"> 문제 제출 </button>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
export default {
  name: "ModifyProblem",
  components:{
    ModifyProblem
  },
  data() {
    return {
      problemID : 0,
      categories: [
        { id: 1, name: "Science" },
        { id: 2, name: "Mathematics" },
        { id: 3, name: "History" },
      ],
      selectedCategory: 1,
      questionName: "",
      selectedQuestionType: "multiple-choice",
      questionContent: "",
      choices: [""],
      currentChoice: "",
      shortAnswer: "",
    };
  },
  computed : {
    token() {
      return this.$route.query.token
    },
    headers() {
      return {Authorization: this.token}
    },
    isAdmin() {
      return this.$route.query.isAdmin
    },
    problemId() {
      return this.$route.params.problemId
    },
  },
  methods: {
    addChoice() {
      this.choices.push("");
    },
    submitQuestion() {
      const question = {
        category: this.selectedCategory,
        name: this.questionName,
        type: this.selectedQuestionType,
        content: this.questionContent,
      };
      if (this.selectedQuestionType === "multiple-choice") {
        question.choices = this.choices;
      } else {
        question.shortAnswer = this.shortAnswer;
      }
      this.$emit('close-modal');
      console.log(question);
      // submit the question to the server or perform some other action here
    },
  },
  mounted() {
    axios.get("http://localhost:8080/mypage/problems/" + this.problemID,
        {
          headers: this.headers
        }).then(response => {
           const data = response
        }).catch(error => {
          alert(error)
    })
  },
};
</script>

<style>
.modal,
.overlay {
  width:100%;
  height:100%;
  position:fixed;
  left:0;
  top:0;
}
.modal {
  line-height:1.5;
}
.overlay {
  opacity: 0.5;
  background-color: black;
}
.modal-card {
  position:relative;
  max-width:80%;
  margin:auto;
  margin-top:30px;
  padding:20px;
  background-color: white;
  min-height:500px;
  z-index:10;
  opacity:1;
}
</style>