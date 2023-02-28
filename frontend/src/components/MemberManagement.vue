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
      <tr v-for="(member, index) in members" :key="member.id">
        <td>{{ member.name }}</td>
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
      <tr v-for="(member, index) in joinList" :key="member.id">
        <td>{{ member.name }}</td>
        <td>
          <button v-if="!member.isAdmin" @click="registerMember(index)">Accept</button>
        </td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import axios from 'axios'
export default {
  data() {
    return {
      members: [
        { id: 1, name: 'John', isAdmin: true },
        { id: 2, name: 'Mary', isAdmin: false },
        { id: 3, name: 'Steve', isAdmin: true },
        { id: 4, name: 'Jane', isAdmin: false },
        { id: 5, name: 'Bob', isAdmin: false },
        { id: 6, name: 'Alice', isAdmin: false },
        { id: 7, name: 'David', isAdmin: true },
        { id: 8, name: 'Sarah', isAdmin: false },
        { id: 9, name: 'Tom', isAdmin: false },
        { id: 10, name: 'Emily', isAdmin: true },
      ],
      joinList: [
        { id: 11, name: 'John' },
        { id: 12, name: 'Alice' },
        { id: 13, name: 'Bob' },
        { id: 14, name: 'Eva' },
        { id: 15, name: 'Mike' },
        { id: 16, name: 'Kate' },
        { id: 17, name: 'David' },
        { id: 18, name: 'Linda' },
        { id: 19, name: 'Chris' },
        { id: 20, name: 'Sarah' }
      ]
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
          .get("http://localhost:8080/groups/" + this.groupId + "/members", {
            headers: this.headers
          })
          .then(response => console.log(response))
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

      axios
          .put("http://localhost:8080/groups/" + this.groupId + "/members", {
            headers: this.headers,
            body: {
              newAdminId: this.members[index].id
            }
      })
          .then(response => {
            console.log(response)
            this.getMembers()
          })
          .catch(error => alert(error))
    },
    deleteMember(index) {
      // /groups/{groupId}/members/{userId}
      const userId = this.members[index].id
      console.log(userId);

      if (!window.confirm(
          `Are you sure you want to dismiss ${this.members[index].name} from this group?\
           This cannot be undone`)) {
        return
      }

      axios
          .delete("http://localhost:8080/groups/" + this.groupId + "/members/" + userId, {
            header: this.headers
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

      const userId = this.joinList[index].id

      axios
          .put("http://localhost:8080/groups/" + this.groupId + "/members/" + userId, {
            header: this.headers
          })
          .then(response => {
            console.log(response)
            this.getMembers()
          })
          .catch(error => alert(error))
    }
  },
  mounted() {
    // this.getMembers()
  }
}
</script>
