<script>
export default {
  name: 'CategoryView', // necessary for self-reference
  props: {
    model: Object
  },
  data() {
    return {
      isOpen: false,
      localModel: null
    }
  },
  computed: {
    isFolder() {
      return this.localModel.children && this.localModel.children.length
    },
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
      if (this.isFolder) {
        this.isOpen = !this.isOpen
      }
    },
    changeType() {
      if (!this.isFolder) {
        this.localModel.children = []
        this.addChild()
        this.isOpen = true
      }
    },
    addChild() {
      this.localModel.children.push({
        name: 'new stuff'
      })
    }
  }
}
</script>

<template>
  <li>
    <div
        :class="{ bold: isFolder }"
        @click="toggle"
        @dblclick="changeType">
      {{ model.name }}
      <span v-if="isFolder">[{{ isOpen ? '-' : '+' }}]</span>
    </div>
    <ul v-show="isOpen" v-if="isFolder">
      <!--
        A component can recursively render itself using its
        "name" option (inferred from filename if using SFC)
      -->
      <CategoryView
          class="item"
          v-for="model in model.children" :key="model.name"
          :model="model">
      </CategoryView>
      <li class="add" @click="addChild">+</li>
    </ul>
  </li>
</template>

<style>
.li {
  list-style-type: none;
  list-style: none;
}
</style>