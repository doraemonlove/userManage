<template>
  <div id="app">
    <div class="page">
      <UserManage></UserManage>
    </div>
  </div>
</template>

<script>
import UserManage from "./components/userManage.vue"

export default {
  name: 'App',
  components: {
    UserManage
  }
}
</script>

<style>
#app {
  width: 100%;
  height: 100%;
  background-color: #F5F8FA;
}
.page {
  /*50 = navbar  */
  min-height: calc(100vh - 50px);
  width: 100%;
  position: relative;
  overflow: hidden;
}

</style>
