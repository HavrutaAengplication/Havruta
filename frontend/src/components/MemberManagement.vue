<template>
  <div>
    <h1>Member List</h1>
    <table>
      <thead>
      <tr>
        <th>Name</th>
        <th>Is Admin?</th>
        <th></th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="(member, index) in members" :key="index">
        <td>{{ member.userName }}</td>
        <td>
          <input v-if="!member.isAdmin" type="checkbox" @click="addAdmin(index)" v-model="member.isAdmin">
          <input v-else type="checkbox" checked disabled>
        </td>
        <td>
          <button v-if="!member.isAdmin" @click="deleteMember(index)">Dismiss</button>
        </td>
      </tr>
      </tbody>
    </table>

    <hr>
  </div>

  <div>
    <h1>Join List</h1>
    <table>
      <thead>
      <tr>
        <th>Name</th>
        <th></th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="(member, index) in joinList" :key="index">
        <td>{{ member.userName }}</td>
        <td>
          <button @click="registerMember(index)">Accept</button>
        </td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import axios from 'axios'
import {BASE_URL, HEADERS} from "@/config";
export default {
  data() {
    return {
      members: [],
      joinList: [],
    }
  },
  computed: {
    groupId() {
      return this.$route.params.groupId;
    },
    token() {
      return this.$route.query.token
    },
    headers() {
      return {Authorization: this.token}
    },
  },
  methods: {
    getMembers() {
      axios
          .get(`${BASE_URL}/groups/${this.groupId}/members`, {
            headers: HEADERS
          })
          .then(response => {
            console.log(response)
            this.members = response.data.memberList
            this.joinList = response.data.joinList
          })
          .catch(error => alert(error))
    },
    addAdmin(index) {
      if (this.members[index].isAdmin) {
        alert("Cannot fire an admin user")
        return
      }

      if (!window.confirm(
          `Are you sure you want to assign ${this.members[index].name} to admin?\
           This cannot be undone`)) {
        return
      }

      console.log("index: " + index)
      console.log(this.members[index].userId)
      axios
          .put(`${BASE_URL}/groups/${this.groupId}/members`,
              { "newAdminId": this.members[index].userId },
              {
                headers: HEADERS,
              })
          .then(response => {
            console.log(response)
            this.getMembers()
          })
          .catch(error => alert(error))
    },
    deleteMember(index) {
      // /groups/{groupId}/members/{userId}
      const userId = this.members[index].userId
      console.log(userId);

      if (!window.confirm(
          `Are you sure you want to dismiss ${this.members[index].name} from this group?\
           This cannot be undone`)) {
        return
      }

      axios
          .delete(`${BASE_URL}/groups/${this.groupId}/members/${userId}`, {
            headers: HEADERS
          })
          .then(response => {
            console.log(response)
            this.getMembers()
          })
          .catch(error => alert(error))
    },
    registerMember(index) {
      // /groups/{groupId}/members/{userId}
      if (!window.confirm(
          `Are you sure you want to register ${this.members[index].name} to this group?\
           This cannot be undone`)) {
        return
      }

      const userId = this.joinList[index].userId

      axios
          .put(`${BASE_URL}/groups/${this.groupId}/members/${userId}`,
              {},
              {
                headers: HEADERS
              })
          .then(response => {
            console.log(response)
            this.getMembers()
          })
          .catch(error => alert(error))
    }
  },
  mounted() {
     this.getMembers()
  }
}
</script>
