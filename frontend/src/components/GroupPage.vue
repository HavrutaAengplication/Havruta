<template>
  <div class="categories">
    <button @click="createCate">CREATE CATEGORY</button>
    <ul>
      <CategoryView class="item" :model="categoryData"></CategoryView>
    </ul>
  </div>
  <div v-if="isAdmin">
    <button @click="OpenAdminPage()"> GROUP ADMIN PAGE </button>
  </div>
</template>

<script>
import CategoryView from "@/components/CategoryView.vue";
import { ref } from 'vue'

const categoryData = ref({
  name: 'Categories',
  children: [
    { name: 'hello' },
    { name: 'world' },
    {
      name: 'child folder',
      children: [
        {
          name: 'child folder',
          children: [{ name: 'hello' }, { name: 'world' }]
        },
        { name: 'hello' },
        { name: 'world' },
        {
          name: 'child folder',
          children: [{ name: 'hello' }, { name: 'world' }]
        }
      ]
    }
  ]
})

export default {
  name: "GroupPage",
  components:{
    CategoryView
  },
  data(){
    return {
      categoryData,
      isAdmin: true
    }
  },
  methods:{
    OpenAdminPage(){

    }
  }

}
</script>

<style>
.item {
  cursor: pointer;
  line-height: 1.5;
}
.bold {
  font-weight: bold;
}
</style>