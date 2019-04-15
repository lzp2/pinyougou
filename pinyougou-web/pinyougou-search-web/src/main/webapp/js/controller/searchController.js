/** 定义搜索控制器 */
app.controller("searchController" ,function ($scope,$sce, baseService) {

    //定义搜索参数对象
    $scope.searchParam = {keywords : '',category : '',brand : '',spec :{},price : ''};
    //定义搜索方法
    $scope.search = function () {
        baseService.sendPost("/Search",$scope.searchParam).then(function (response) {
            //获取搜索结果 response.data:{total:100,rows:{},{}}
            $scope.resultMap = response.data;

            //页面显示的关键字
            $scope.keyword = $scope.searchParam.keywords;
        });
    };

    //讲文本转换成html
    $scope.trustHtml = function (html) {
        return $sce.trustAsHtml(html);
    };

    //添加过滤条件方法
    $scope.addSearchItem = function (key, value) {
        //判断是商品分类,品牌,价格
        if (key == 'category' || key == 'brand' || key == 'price'){
            $scope.searchParam[key] = value;
        }else {
            //规格选项
            $scope.searchParam.spec[key] = value;
        }
        //执行搜索
        $scope.search();
    };

    //删除过滤条件方法
    $scope.removeSearchItem = function (key) {
        //判断是商品分类,品牌,价格
        if (key == "category" || key == "brand" || key == "price"){
            $scope.searchParam[key] = "";
        }else {
            //删除规格选项
            delete $scope.searchParam.spec[key];
        }
        //执行搜索
        $scope.search();
    };

    //获取首页传过来的关键字
});
