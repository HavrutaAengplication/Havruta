<template>
<div>
  <button><router-link :to="'/groups/' + this.groupId + '/members'">회원 관리</router-link></button>
</div>

<div>
  <button @click="removeGroup">그룹 삭제</button>
</div>

<div>
  <button @click="showPopup">그룹 정보 수정</button>
  <modifyGroupInfo v-if="popupModifyGroupVisible" @close-popup="hidePopup" @send-group-name="modifyGroupInfo" />

</div>

<div>
  <button @click="removeCategory">카테고리 삭제</button>
</div>
</template>

<script>
import axios from 'axios'
import {BASE_URL, HEADERS} from "@/config";
import ModifyGroupInfo from '@/components/ModifyGroupInfo.vue'

export default {
  name: "GroupAdmin",
  inheritAttrs: false,
  components: {
    ModifyGroupInfo
  },
  data() {
    return {

      popupModifyGroupVisible: false,

    }
  },
  computed: {
    token() {
      return this.$route.query.token
    },
    headers() {
      return {Authorization: this.token}
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
    modifyGroupInfo(name) {
      console.log("New Group Name: " + name);
      axios.
        put(`${BASE_URL}/groups/${this.groupId}`,
          { newGroupName: name },
          {
            headers: HEADERS
          })
          .then(response => {
            const data = response
            console.log(data)
          })
          .catch(error => {
            alert(error)
          })

    },
    removeGroup() {
      axios.
        delete(`${BASE_URL}/groups/${this.groupId}`,
          {
            headers: this.headers
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
      /*
          groupId로 json file 안에서 group 안의 category 모두 가져오기
          그 중 삭제할 category 선택 후 categoryId 설정
       */

      axios.
        delete(`${BASE_URL}/groups/${this.groupId}/categories/${this.categoryId}`,
          {
            headers: this.headers
          })
          .then(response => {
            const data = response
            console.log(data)
          })
          .catch(error => {
            alert(error)
          })
    },
    showPopup() {
      this.popupModifyGroupVisible = true;
      this.freezeBody();
    },
    hidePopup() {
      this.popupModifyGroupVisible = false;
      this.unfreezeBody();
    },
    freezeBody() {
      document.body.style.overflow = 'hidden';
    },
    unfreezeBody() {
      document.body.style.overflow = 'auto';
    },
  }
}
</script>

<style scoped>

</style>