/** 定义控制器层 */
app.controller('typeTemplateController', function($scope, $controller, baseService){

    /** 指定继承baseController */
    $controller('baseController',{$scope:$scope});

    /** 查询条件对象 */
    $scope.searchEntity = {};
    /** 分页查询(查询条件) */
    $scope.search = function(page, rows){
        baseService.findByPage("/typeTemplate/findByPage", page,
			rows, $scope.searchEntity)
            .then(function(response){
                /** 获取分页查询结果 */
                $scope.dataList = response.data.rows;
                /** 更新分页总记录数 */
                $scope.paginationConf.totalItems = response.data.total;
            });
    };

    /** 添加或修改 */
    $scope.saveOrUpdate = function(){
        var url = "save";
        if ($scope.entity.id){
            url = "update";
        }
        /** 发送post请求 */
        baseService.sendPost("/typeTemplate/" + url, $scope.entity)
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
       //把品牌json数组的字符串转化成json数组
        $scope.entity.brandIds = JSON.parse(entity.brandIds);
        //把规格JSON数组的字符串转化成JSON数组
        $scope.entity.specIds = JSON.parse(entity.specIds);
        //把扩展属性JSON数组的字符串转化成JSON数组
        $scope.entity.customAttributeItems = JSON.parse(entity.customAttributeItems);
    };

    /** 批量删除 */
    $scope.delete = function(){
        if ($scope.ids.length > 0){
            baseService.deleteById("/typeTemplate/delete", $scope.ids)
                .then(function(response){
                    if (response.data){
                        /** 重新加载数据 */
                        $scope.reload();
                        $scope.ids = [];
                    }else{
                        alert("删除失败！");
                    }
                });
        }else{
            alert("请选择要删除的记录！");
        }
    };

    //品牌列表数据
    //$scope.brandList = {data:[{id:1,text:'联想'},{id:2,text:'华为'},{id:3,text:'小米'}]};
    //品牌列表
    $scope.findBrandList = function () {
        baseService.sendGet("/brand/findBrandList").then(function (response) {
            $scope.brandList = {data:response.data};
        });
    };

    //规格列表
    $scope.findSpecList = function () {
        baseService.sendGet("/specification/findSpecList").then(function (response) {
            $scope.specList = {data:response.data};
        });
    };

    //新增扩展属性行
    $scope.addTableRow = function () {
        $scope.entity.customAttributeItems.push({});
    };
    //删除扩展属性行
    $scope.deleteTableRow = function (idx) {
        $scope.entity.customAttributeItems.splice(idx,1);
    };

    //提取数组中JSON某个属性(提取文本),返回拼接的字符串(逗号分隔)
    $scope.jsonArr2Str = function (jsonArrStr, key) {
        //把JSONArrStr转化成JSON数组对象
        var jsonArr = JSON.parse(jsonArrStr);
        //定义新数组
        var resArr = [];
        //迭代新数组
        for (var i = 0;i <jsonArr.length; i++){
            //取数组中的一个元素
            var json = jsonArr[i];
            //把JSON对象的值添加到新数组
            resArr.push(json[key]);
        }
        //返回数组中的元素用逗号分隔的字符串
        return resArr.join(",");
    }
});