<script>
import axios from 'axios'
export default {
  name: 'CategoryView', // necessary for self-reference
  props: {
    model: Object
  },
  data() {
    return {
      isOpen: false,
      localModel: null,
      newCateName: '',
      openNewCategory: false,
    }
  },
  computed: {
    thisModel() {
      return this.localModel;
    }
  },
  created() {
    // create a copy of the prop when the component is created
    this.localModel = Object.assign({}, this.model)
  },
  methods: {
    toggle() {
        this.isOpen = !this.isOpen
    },
    changeName() {
      this.localModel.name = this.newCateName
    },
    createCategory() {
      this.localModel.children.push({
        name: '새 카테고리'
      })
      // axios로 카테고리 생성 request 해주는 구문 넣어야 함!

      this.openNewCategory = false
    },
    openCreateCategory(){
      this.openNewCategory = true
    }
  }
}
</script>

<template>
  <li>
    <div class="bold" @click="toggle" @dblclick="changeName">
      {{ model.name }}
      <span>[{{ isOpen ? '-' : '+' }}]</span>
    </div>
    <ul v-show="isOpen">
      <CategoryView class="item" v-for="model in model.children" :key="model.name" :model="model">
      </CategoryView>
      <div v-if="openCreateCategory()">
        <input v-model="newCateName" @keyup.enter="createCategory()" placeholder="새 카테고리 이름">
      </div>
      <div v-else>
        <button class="add" @click="openCreateCategory()">Add New Category</button>
      </div>
    </ul>
  </li>
</template>

<style>
.li {
  list-style-type: none;
  list-style: none;
}
</style>