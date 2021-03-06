/** 定义控制器层 */
app.controller('itemCatController', function($scope, $controller, baseService){

    /** 指定继承baseController */
    $controller('baseController',{$scope:$scope});

    //根据上级ID显示下级商品分类列表
    $scope.findItemCatByParentId = function (parentId) {
        baseService.sendGet("/itemCat/findItemCatByParentId?parentId=" + parentId).then(function (response) {
            $scope.dataList = response.data;
        });
    };

    //默认为1级
    $scope.grade = 1;
    //查询下级
    $scope.selectList = function (entity, grade) {
        $scope.grade = grade;
        //如果为1级
        if (grade == 1){
            $scope.itemCat_1 = null;
            $scope.itemCat_2 = null;
        }
        //如果为2级
        if (grade == 2){
            $scope.itemCat_1 = entity;
            $scope.itemCat_2 = null;
        }
        //如果为3级
        if (grade == 3){
            $scope.itemCat_2 = entity;
        }

        /** 查询此级下级列表 */
        $scope.findItemCatByParentId(entity.id);
    };
    /** 添加或修改 */
    $scope.saveOrUpdate = function(){
        var url = "save";
        if ($scope.entity.id){
            url = "update";
        }
        /** 发送post请求 */
        baseService.sendPost("/itemCat/" + url, $scope.entity)
            .then(function(response){
                if (response.data){
                    /** 重新加载数据 */
                    $scope.reload();
                }else{
                    alert("操作失败！");
                }
            });
    };

    /** 显示修改 */
    $scope.show = function(entity){
       /** 把json对象转化成一个新的json对象 */
       $scope.entity = JSON.parse(JSON.stringify(entity));
    };

    /** 批量删除 */
    $scope.delete = function(){
        if ($scope.ids.length > 0){
            baseService.deleteById("/itemCat/delete", $scope.ids)
                .then(function(response){
                    if (response.data){
                        /** 重新加载数据 */
                        $scope.reload();
                    }else{
                        alert("删除失败！");
                    }
                });
        }else{
            alert("请选择要删除的记录！");
        }
    };
});