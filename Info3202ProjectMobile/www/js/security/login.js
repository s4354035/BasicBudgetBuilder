/**
 * Created by Hanzi Jing on 7/05/2017.
 */
angular.module('mainApp')
    .controller('loginController', [ '$state', '$scope', '$rootScope', '$http', 'BASE_CONSTS', function($state, $scope, $rootScope, $http, BASE_CONSTS) {
        var view = $scope;
        var authenticate = function (credentials, callback) {
            if(credentials) {
                var headers = credentials ? {
                    authorization: "Basic " + btoa(credentials.username + ":" + credentials.password)} : {};
                $http.get(BASE_CONSTS.ip + 'user', {headers: headers}).then(function (response) {
                    $rootScope.authenticated = response.data.name;
                    callback && callback();
                }, function () {
                    $rootScope.authenticated = false;
                    callback && callback();
                });
            }else{
                $rootScope.authenticated = false;
                callback && callback();
            }
        };
        authenticate();
        view.credentials = {};
        view.login = function () {
            authenticate(view.credentials, function () {
                if ($rootScope.authenticated) {
                    // $location.path("/home");
                    $rootScope.fromLogin = true;
                    $rootScope.$broadcast('refresh_statistics');
                    $state.go("home");
                    view.error = false;
                } else {
                    $state.go("login");
                    // $location.path("/login");
                    view.error = true;
                }
            });
        };
        view.logout = function () {
            $http.post(BASE_CONSTS.ip + 'logout', {}).finally(function () {
                $rootScope.authenticated = false;
                $state.go("login");
            });
        };
    }]);