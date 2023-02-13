<template>
  <div class="close-image">
    <button @click="closePopup()">close</button>
  </div>
</template>

<script>
export default {
  methods:{
    closePopup: function(){
      this.$emit('close-popup')
    }
  }
}
</script>

<style>

</style>