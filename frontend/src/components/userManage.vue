<template>
  <div>
    <el-header height="32px" style="margin: 16px 0">
      <el-input v-model="prompt"
                style="margin-right: 16px; width: 200px; height: 48px"
                placeholder="请输入要搜索的用户名"
                suffix-icon="el-icon-edit"
                clearable
                @keyup.enter.native="handleSearch"/>
      <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
      <el-button :disabled="batchItems.length !== 0" type="primary" icon="el-icon-plus" @click="handleCreate">创建</el-button>
      <el-button :disabled="batchItems.length === 0" type="danger" icon="el-icon-delete" @click="handleBatchDelete">删除</el-button>
    </el-header>
    <el-divider />
    <el-main>
      <el-table :data="tableData" style="width: 100%" @selection-change="handleBatchSelection">
        <el-table-column type="selection" width="55" />
        <el-table-column label="用户编号" width="80">
          <template slot-scope="scope">
            <b style="padding-left: 2em">{{ scope.row.id }}</b>
          </template>
        </el-table-column>
        <el-table-column label="用户名">
          <template slot-scope="scope">
            <span>{{ scope.row.username }}</span>
          </template>
        </el-table-column>
        <el-table-column label="已授予角色">
          <template slot-scope="scope">
            <el-tag style="margin-right: 5px" type="success">{{ scope.row.authority }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column width="180" label="操作">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleEdit(scope.$index, scope.row)">修改</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <footer>
        <el-pagination
            :current-page.sync="pageNum"
            :page-size.sync="pageSize"
            :total="total"
            style="margin-top: 16px;"
            background
            align="right"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"/>
      </footer>
    </el-main>
    <el-dialog :title="isEditState ? '修改用户信息' : '创建用户'" :visible.sync="dialog.isShow" width="30%" :before-close="handleClose">
      <el-form label-width="100px">
        <el-form-item label="用户名*">
          <el-input v-model="item.username" clearable required />
        </el-form-item>
        <el-form-item v-if="isEditState && !isSetPassword">
          <el-button type="danger" @click="isSetPassword = true">修改密码</el-button>
        </el-form-item>
        <el-form-item v-else label="密码*">
          <el-input v-model="item.password" clearable required />
        </el-form-item>
        <el-form-item label="授权角色">
          <el-select v-model="item.authority" placeholder="请选择">
            <el-option v-for="role in roleTypes" :key="role" :label="role" :value="role" />
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialog.isShow = false">取 消</el-button>
        <el-button type="primary" @click="handleChange">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
import CryptoJS from 'crypto-js'
import { getUserList, queryUserByName, addOrUpdateUser, deleteUser } from '@/api/manage'
export default {
  data() {
    return {
      prompt: '', // 搜索提示
      dialog: {
        isShow: false // 是否显示对话框
      },
      tableData: [], // 原始数据
      batchItems: [],
      item: {},
      pageNum: 1,
      pageSize: 10,
      total: 0,
      roleTypes: ['admin', 'guest'],
      isEditState: false, // edit state
      isSetPassword: false // 是否修改密码
    }
  },
  mounted() {
    this.fetchUsers()
  },
  methods: {
    handleCurrentChange(newPage) {
      this.pageNum = newPage
      this.fetchUsers()
    },
    handleSizeChange(newSize) {
      this.pageSize = newSize
      this.fetchUsers()
    },
    fetchUsers() {
      console.log(this.pageNum, this.pageSize)
      // fetch
      getUserList({'page': this.pageNum - 1, 'size': this.pageSize}).then((data) => {
        console.log(data)
        this.tableData = data.data.content
        this.total = data.data.totalElements
      })
    },
    handleCreate() {
      // create template
      this.item = {
        id: '',
        username: '',
        password: '',
      }
      // show dialog
      this.dialog.isShow = true

      // set state to no edit
      this.isEditState = false
    },
    handleChange() {
      if (this.item.username === '') {
        // 不可为空
        this.$alert('用户名不能为空')
        return
      }
      if (this.item.password === '') {
        // 不可为空
        this.$alert('密码不能为空')
        return
      }
      this.item.password = CryptoJS.AES.encrypt(this.item.password, CryptoJS.enc.Utf8.parse('nkcs@@@@aiops@!3'), {
        mode: CryptoJS.mode.ECB,
        padding: CryptoJS.pad.ZeroPadding
      }).toString()
      if (this.isEditState) {
        addOrUpdateUser(this.item).then(() => {
          this.fetchUsers()
        })
      } else {
        addOrUpdateUser(this.item).then(() => {
          this.fetchUsers()
        })
      }
      this.dialog.isShow = false
    },
    handleEdit(index, row) {
      this.isSetPassword = false
      this.isEditState = true
      this.item = {
        ...row
      }
      // show dialog
      this.dialog.isShow = true
      // update
    },
    handleDelete(index, row) {
      this.$confirm('确认删除id为' + row.id + '的用户？')
          .then(() => {
            deleteUser([row.id]).then(() => {
              this.fetchUsers()
            })
          })
          .catch((_) => {})
    },
    handleClose(done) {
      this.$confirm('确认关闭？')
          .then((_) => {
            done()
          })
          .catch((_) => {})
    },
    handleSearch() {
      queryUserByName({'name': this.prompt, 'page': this.pageNum - 1, 'size': this.pageSize}).then((data) => {
        this.tableData = data.data.content
        this.total = data.data.totalElements
      })
    },
    handleBatchSelection(value) {
      this.batchItems = value
    },
    handleBatchDelete() {
      const params = this.batchItems.map((item) => item.id)
      this.$confirm('确认批量删除id为' + params.join(',') + '的用户？')
          .then(() => {
            deleteUser(params).then(() => {
              this.fetchUsers()
            })
          })
    }
  }
}
</script>
