/**
 * Created by Hanzi Jing on 7/05/2017.
 */
angular.module('mainApp')
    .controller('fullTextSearchController',[ '$scope', '$http', '$state', '$rootScope', 'BASE_CONSTS',
        function($scope, $http, $state, $rootScope, BASE_CONSTS){

        var view = $scope;

        view.search = function(){
            if($scope.searchText == null || $scope.searchText == ""){
                return;
            }
            $rootScope.searchResults = [];
            $rootScope.searchResultMessage ="Searching ...";
            $http.post(BASE_CONSTS.ip + 'fullTextSearch', view.searchText)
                .then(
                    function (response) {
                        if(response != null && response.data != null && response.data.length > 0){
                            $rootScope.searchResults = response.data;
                            $rootScope.searchResultMessage = "There are " + $rootScope.searchResults.length + " items found";
                        }
                        else{
                            $rootScope.searchResultMessage ="No thing found";
                        }
                        $rootScope.$broadcast("refreshSearch");
                        $state.go("home.search");
                    },
                    function () {
                        if(response!= null && response.data != null && response.data.session != null){
                            dialogs.error("ERROR",  response.data.session, {size:'sm'});
                            $state.go("login");
                        }
                        else {
                            $rootScope.searchResultMessage = "No thing found";
                            $rootScope.$broadcast("refreshSearch");
                            $state.go("home.search");
                        }
                    });
        }
    }]);