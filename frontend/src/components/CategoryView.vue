<script>
import axios from 'axios'
import {BASE_URL, HEADERS} from "@/config";

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
      newCateId : 0,
      curRenameOpen: -1,
      curNewCateOpen: -1,
    }
  },
  computed: {
    thisModel() {
      return this.localModel
    }
  },
  created() {
    this.localModel = Object.assign({}, this.model)
  },
  methods: {
    createCate(m) {
      axios
          .post(`${BASE_URL}/groups/${this.groupId}/categories`,
              {
            parentCategoryId: m.categoryId,
            categoryName: this.newCateName
              },
              {
                headers: HEADERS
              }
          )
          .then(response => {
            console.log(response)
          })
      this.curNewCateOpen = -1
      this.$emit('get-group-data');
      this.localModel = Object.assign({}, this.model)
    },
    RenameCate(m) {
      this.curRenameOpen = -1
      axios
          .put(`${BASE_URL}/groups/${this.groupId}/categories`,
              {
                parentCategoryId: m.categoryId,
                categoryName: this.newCateName
              },
              {
                headers: HEADERS
              }
          )
          .then(response => {
            console.log(response)
          })
          .catch(error => {
            alert(error)
          })
      this.$emit('get-group-data');
      this.localModel = Object.assign({}, this.model)
    },
    DeleteCate(m) {
      this.thisModel.splice(m.categoryId - 1, 1)
      axios
          .delete(`${BASE_URL}/groups/${this.groupId}/categories/${m.categoryId}`,
              {
                headers: HEADERS
              })
          .then(response => {
            console.log(response)
          })
          .catch(error => {
            alert(error)
          })
      this.$emit('get-group-data');
      this.localModel = Object.assign({}, this.model)
    },
  }
}
</script>

<template>
  <li>
    <div v-for="m in model" :key="m.categoryId">
      <span v-if="curRenameOpen != m.categoryId">
        {{ m.categoryName }} <t></t>
      </span>
      <input v-if="curRenameOpen == m.categoryId" @keyup.enter="RenameCate(m)">
      <button v-if="curNewCateOpen != m.categoryId" @click="curNewCateOpen = m.categoryId"> ADD SUBCATEGORY </button>
      <button v-if="curRenameOpen != m.categoryId" @click="curRenameOpen = m.categoryId" placeholder="m.categoryId"> RENAME </button>
      <button @click="DeleteCate(m)"> DELETE </button><br>
      <input v-if="curNewCateOpen == m.categoryId" v-model="newCateName" @keyup.enter="createCate(m)" placeholder="NEW CATEGORY NAME">
    </div>
  </li>
</template>

<style>
.li {
  list-style-type: none;
  list-style: none;
}
</style>