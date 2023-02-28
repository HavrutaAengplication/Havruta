<template>
  <div v-for="(problem, pindex) in problems" :key="pindex">
    <div class="problem-number">
      <h2>Problem {{ pindex + 1 }}</h2>
<!--        <div v-if="problem.correct" class="answer-correct">-->
<!--          O-->
<!--        </div>-->
    </div>
    <p class="problem-description">{{ problem.problemQuestion }}</p>

    <div><h1></h1></div>

    <div class="items">
      <div v-if="problem.problemType==0">
        <ul>
          <li v-for="(item, itemIndex) in problem.problemCandidateList" :key="itemIndex">
            <label>
              <input type="radio" v-model="this.userAnswer[pindex]" :value="item" />
              {{ item }}
            </label>
          </li>
        </ul>
      </div>
      <div v-else>
        <label></label>
        <input v-model="this.userAnswer[pindex]" placeholder="Your Answer" />
      </div>
    </div>

    <div><h1></h1></div>

    <div class="left-align">
      <button @click="scoreProblem(pindex)">Score</button>
    </div>
    <div v-if="this.scored[pindex]">
      <span :class="{correct: this.correct[pindex], incorrect: !this.correct[pindex]}">
        {{ this.correct[pindex] ? "O" : "X" }}
      </span>
    </div>
    <div class="problem-devider"></div>
  </div>
  <div class="left-align">
    <button @click="scoreAll">Score All</button>
  </div>
</template>

<script>
 import axios from 'axios';
 import {BASE_URL, HEADERS} from "@/config";

export default {
  data() {
    return {
      problems: [],
      userAnswer: [],
      scored: [],
      correct: [],
      categoryProblemList : [
        {
          problemId: 0,
          problemType: 0,
          problemQuestion: "P1 descriptionP1 descriptionP1 descriptionP1 description" +
              "P1 descriptionP1 descriptionP1 descriptionP1 descriptionP1 descriptionP1 description" +
              "P1 descriptionP1 descriptionP1 descriptionP1 descriptionP1 descriptionP1 description",
          problemCandidateList: [ "Item1", "Item2", "Item3", "Item4" ],
          problemAnswer: "Item1",
          problemImage: [ "imgsrc1", "imgsrc2" ],
          userAnswer: "",
          selectedItem: "",
          scored: false,
          correct: false,
          showItems: false,
        },
        {
          problemId: 1,
          problemType: 1,
          problemQuestion: "P2 description",
          problemCandidateList: [ "Item1", "Item2", "Item3", "Item4" ],
          problemAnswer: "Answer",
          problemImage: [ "imgsrc1", "imgsrc2" ],
          userAnswer: "",
          selectedItem: "",
          scored: false,
          correct: false,
          showItems: false,
        },
        {
          problemId: 2,
          problemType: 0,
          problemQuestion: "P3 description",
          problemCandidateList: [ "Item1", "Item2", "Item3", "Item4" ],
          problemAnswer: "Item3",
          problemImage: [ "imgsrc1", "imgsrc2" ],
          userAnswer: "",
          selectedItem: "",
          scored: false,
          correct: false,
          showItems: false,
        },
      ]
    }
  },
  computed: {
    groupId() {
      return this.$route.params.groupId;
    },
    categoryId() {
      return this.$route.params.categoryId;
    }
  },
  mounted() {
    let numProblem = this.categoryProblemList.length
    this.getProblems()
    this.userAnswer = new Array(numProblem).fill(0)
    this.scored = new Array(numProblem).fill(false)
    this.correct = new Array(numProblem).fill(false)
  },
  methods: {
    getProblems() {
      axios
          .get(`${BASE_URL}/groups/${this.groupId}/categories/${this.categoryId}`, {
            headers: HEADERS
          })
          .then(response => {
            console.log(response)
            this.problems = response.categoryProblemList
          })
    },
    toggleItems(index) {
      this.problems[index].showItems = !this.problems[index].showItems;
    },
    scoreProblem(index) {
      this.correct[index] = (this.userAnswer === this.problems[index].problemAnswer)
      this.scored[index] = true;
    },
    scoreAll() {
      const numProblems = this.problems.length;
      for (let i=0; i<numProblems; i++)
        this.scoreProblem(i);
    }
  },
}
</script>

<style>
.correct {
  color: green;
}
.incorrect {
  color: red;
}

.left-align {
  display: flex;
  align-items: center;

}

.problem {
  display: flex;
  align-items: center;
}

.problem-number {
  margin: 0;
  font-size: 2rem;
  font-weight: bold;
  text-align: left;
}

.answer-correct {
  position: relative;
  top: -1.5em;
  font-size: 3em;
  color: green;
}

.problem-description {
  margin: 0;
  margin-left: 1rem;
  font-size: 1.5rem;
  white-space: normal;
  word-wrap: anywhere;
  max-width: 80%;
  overflow: hidden;
  text-align: left;
}

.items {
  margin-left: 20px;
  display: flex;
  align-items: center;
}


.problem-devider {
  border-top: 1px solid black;
  margin-top: 20px;
  margin-bottom: 20px;
}

h2 {
  display: inline-block;
  margin-right: 10px;
  font-size: 24px;
  font-weight: bold;
}
ul {
  list-style: none;
  margin: 0;
  padding: 0;
}
li {
  margin-bottom: 5px;
}
label {
  display: inline-block;
  margin-right: 10px;
}

</style>