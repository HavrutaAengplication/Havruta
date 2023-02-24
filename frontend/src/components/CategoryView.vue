<template>
  <li>
    <div :class="{ bold: isFolder }" @click="toggle" @dblclick="changeType">
      {{ model.name }}
      <span v-if="isFolder">[{{ isOpen ? '-' : '+' }}]</span>
    </div>
    <ul v-show="isOpen" v-if="isFolder">
      <!--CategoryView class="item" v-for="model in model.children" :model="model"></CategoryView-->
      <li class="add" @click="addChild">+ADD CATEGORY</li>
    </ul>
  </li>
</template>

<script>
export default {
  //name: 'CategoryView', // necessary for self-reference
  props: {
    model: Object
  },
  data() {
    return {
      isOpen: false
    }
  },
  computed: {
    isFolder() {
      return this.model.children && this.model.children.length
    }
  },
  methods: {
    toggle() {
      if (this.isFolder) {
        this.isOpen = !this.isOpen
      }
    },
    changeType() {
      if (!this.isFolder) {
        //this.model.children = []
        this.addChild()
        this.isOpen = true
      }
    },
    addChild() {
      //this.model.children.push({
        //name: 'new stuff'
      //})
    }
  }
}
</script>

<style scoped>

</style>