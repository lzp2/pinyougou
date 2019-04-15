/** 定义首页控制器层 */
app.controller("indexController", function($scope, baseService){
    
    //根据广告分类id查询广告内容
    $scope.findContentByCategoryId = function (categoryId) {
        //查询首页广告数据
        baseService.sendGet("/content/findContentByCategoryId?categoryId=" + categoryId).then(function (response) {
            //获取响应数据 List<Content> [{},{}]
            $scope.contentList = response.data;
        });
    }
});