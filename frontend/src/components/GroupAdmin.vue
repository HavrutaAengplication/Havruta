<template>
<div>
  <button>회원 관리</button>
</div>

<div>
  <button @click="removeGroup">그룹 삭제</button>
</div>

<div>
  <button>그룹 정보 수정</button>
</div>

<div>
  <button @click="removeCategory">카테고리 삭제</button>
</div>
</template>

<script>
import axios from 'axios'
import ModifyGroupInfo from '@/components/ModifyGroupInfo.vue'

export default {
  name: "GroupAdmin",
  inheritAttrs: false,
  data() {
    return {
      popupModifyGroupVisible: false,

    }
  },
  computed: {
    token() {
      return this.$route.query.token
    },
    isAdmin() {
      return this.$route.query.isAdmin
    },
    groupId() {
      return this.$route.params.groupId
    },
    categoryId() {
      return this.$route.params.categoryId
    }
  },
  mounted() {
    if (!this.isAdmin) {
      console.log('Admin isAdmin: ' + this.isAdmin)
      alert('You are not authorized to access this page.');
      this.$router.go(-1);  // go back
    }
    // console.log('route: ' + this.$route)
    // console.log('router: ' + this.$router)
  },
  methods: {
    removeGroup() {
      axios.
        delete("http://localhost:8080/groups/" + this.groupId,
          {
            headers: {
              token: ""
            }
          })
          .then(response => {
            const data = response
            console.log(data)
          })
          .catch(error => {
            alert(error)
          })
    },
    removeCategory() {
      axios.
        delete("http://localhost:8080/groups/" +
          this.groupId + "/categories/" + this.categoryId,
          {
            headers: {
              token: ""
            }
          })
          .then(response => {
            const data = response
            console.log(data)
          })
          .catch(error => {
            alert(error)
          })
    },
  }
}
</script>

<style scoped>

</style>