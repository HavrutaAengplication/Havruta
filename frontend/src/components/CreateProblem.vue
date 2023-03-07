<template>
  <div class="modal">
    <div class="overlay" @click="$emit('close-modal')"></div>
    <div class="modal-card">
      <h1>새로운 문제 생성</h1>
      <div>
        <label for="category">문제 카테고리 : </label>
        <select id="category" v-model="selectedCategory" multiple>
          <option v-for="category in categories" :key="category.categoryId" :value="category.categoryName" @click="assignCategory()">{{ category.categoryName }}</option>
        </select>
        <div> 선택된 카테고리 : {{ selectedCategories }}</div>
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
        <label for="question-content">문제 내용 : </label><br>
        <textarea id="question-content" v-model="questionContent"></textarea>
      </div>
      <div>
        <ul class="footer-button-plus">
          <input @change="upload" type="file" id="file" accept="image/*" class="inputFile" />
          <label for="file" class="input-plus">+</label>
        </ul>
      </div>
      <div v-if="selectedQuestionType === 'multiple-choice'">
        <div v-for="(choice, index) in choices" :key="index">
          <input type="radio" :id="choice.index" :value="choice" v-model="checkedChoices"> <label>{{ index+1  }} : {{ choice }}</label>
        </div>
        <input v-if="openNewChoice == true" v-model="newChoiceContent" @keyup.enter="addChoice()">
        <button v-if="openNewChoice == false" @click="openNewChoice = true">Add Choice</button>
        <div> 선택된 정답 : {{ checkedChoices }}</div>
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
import {BASE_URL, HEADERS} from "@/config";

export default {
  name: 'CategoryComponent',
  data() {
    return {
      categories: [],
      selectedCategories : [],
      selectedCategory: 1,
      questionName: "",
      selectedQuestionType: "multiple-choices",
      questionContent: "",
      choices: [],
      checkedChoices: [],
      currentChoice: "",
      shortAnswer: "",
      answer: "",
      images: [],
      openNewChoice: false,
      newChoiceContent: "",
    };
  },
  methods: {
    upload(e){
      let ImageFile = e.target.files
      let url=URL.createObjectURL(ImageFile[0])
      this.images.push({
        image: url
      })
    },
    assignCategory() {
      this.selectedCategories.push({
        categoryId: this.selectedCategory
      })
    },
    addChoice() {
      this.choices.push({
        item: this.newChoiceContent
      })
      this.newChoiceContent = ""
      this.openNewChoice = false
    },
    submitQuestion() {
      this.$emit('close-modal');

      if (this.selectedQuestionType == "multiple-choices"){
        this.answer = this.checkedChoices
      }
      else {
        this.answer = this.shortAnswer
      }

      axios.
      post(`${BASE_URL}/groups/${this.groupId}/problems`, {
        headers : this.headers,
        body : {
          categoryIdList : this.categories,
          problemQuestion : this.questionName,
          problemType : this.selectedQuestionType,
          problemCandidate : this.choices,
          problemAnswer : this.answer,
          problemImage : this.images
        }
      }).then(response => {
        const data = response
        console.log(data)
      }).catch(error => {
        alert(error)
      })
      // submit the question to the server or perform some other action here
    },
  },
  computed : {
    token() {
      return this.$route.query.token
    },
    headers() {
      return {Authorization: this.token}
    },


    groupId() {
      return this.$route.params.groupId
    },
  },
  created() {
    axios.get(`${BASE_URL}/groups/${this.groupId}`,
        {
          headers: HEADERS
        }).then(response => {
      this.categories = response.data.categoryList
        }).catch(error => {
          alert(error)
    })
  }
};
</script>

<style>
div {
  padding-top: 15px;
}

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
