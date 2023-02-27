<template>
  <div v-for="(problem, index) in problems" :key="index">
    <div class="problem">
      <div class="problem-number">
        <h2>Problem {{ index + 1 }}</h2>
<!--        <div v-if="problem.correct" class="answer-correct">-->
<!--          O-->
<!--        </div>-->
      </div>
      <p class="problem-description">{{ problem.problemQuestion }}</p>
    </div>

    <div class="items">
      <div v-if="problem.problemType==0">
        <ul>
          <li v-for="(item, itemIndex) in problem.problemCandidateList" :key="itemIndex">
            <label>
              <input type="radio" v-model="problem.selectedItem" :value="item" />
              {{ item }}
            </label>
          </li>
        </ul>
      </div>
      <div v-else>
        <label></label>
        <input v-model="problem.userAnswer" />
      </div>
    </div>
    <div class="left-align">
      <button @click="scoreProblem(index)">Score</button>
    </div>
    <div v-if="problem.scored">
      <span :class="{correct: problem.correct, incorrect: !problem.correct}">
        {{ problem.correct ? "O" : "X" }}
      </span>
    </div>
    <div class="problem-devider"></div>
  </div>
  <div class="left-align">
    <button @click="scoreAll">Score All</button>
  </div>
</template>

<script>
// import axios from 'axios';

export default {
  data() {
    return {
      problems: [],
      solutions: [],
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
  mounted() {
    this.problems = this.categoryProblemList;
    // axios.get('/api/problems')
    //     .then(response => {
    //       this.problems = response.data;
    //       this.selected = new Array(this.problems.length).fill(-1);
    //     });
  },
  methods: {
    toggleItems(index) {
      this.problems[index].showItems = !this.problems[index].showItems;
    },
    scoreProblem(index) {
      const problem = this.problems[index];
      if (problem.problemType==0) {
        if (problem.selectedItem === problem.problemAnswer) {
          problem.correct = true;
        } else {
          problem.correct = false;
        }
      }
      else {
        if (problem.userAnswer === problem.problemAnswer)
          problem.correct = true;
        else
          problem.correct = false;
      }
      problem.scored = true;
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