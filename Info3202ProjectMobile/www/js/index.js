angular.module('mainApp', [ 'ui.router', 'ngTouch', 'ui.grid', 'ui.grid.pagination', 'ui.grid.selection', 'ui.grid.exporter',"chart.js",'ui.bootstrap','dialogs.main','pascalprecht.translate','dialogs.default-translations'])
    .constant('BASE_CONSTS', {
        // IP address for connecting to cloud service
        ip: 'https://infs3202.herokuapp.com/'

        // IP address for connecting to local service
        // ip: 'http://192.168.31.189:8080/'
    })
    .config(function($stateProvider,  $httpProvider, $translateProvider) {
        $translateProvider.useSanitizeValueStrategy('sanitize');
        var loginState = {
            name: 'login',
            controller : 'loginController',
            templateUrl: 'mobile/login.html'
        };
        var homeState = {
            name: 'home',
            controller : 'homeController',
            templateUrl: 'mobile/home.html'
        };
        var registerState = {
            name: "register",
            controller : 'registrationController',
            templateUrl: 'mobile/register1.html'
        };
        var forgotPasswordState = {
            name: "forgot_password",
            controller : 'forgotPasswordController',
            templateUrl: 'mobile/forgot_password.html'
        };
        var budgetState = {
            name: "home.budget",
            controller : 'budgetController',
            templateUrl: 'mobile/budget1.html'
        };
        var debitState = {
            name: "home.debit",
            controller : 'debitController',
            templateUrl: 'mobile/debit1.html'
        };
        var autoDebitState = {
            name: "home.autoDebit",
            controller : 'autoDebitController',
            templateUrl: 'mobile/auto_debit.html'
        };
        var statisticsState = {
            name: "home.statistics",
            controller : 'statisticsController',
            templateUrl: 'mobile/statistics1.html'
        };
        var searchState = {
            name: "home.search",
            controller : 'searchResultController',
            templateUrl: 'mobile/fullTextSearch1.html'
        };
        var budgetEditState = {
            name: "createEditBudget",
            controller : 'createEditBudgetController',
            templateUrl: 'mobile/create_edit_budget.html'
        };
        var debitEditState = {
            name: "createEditDebit",
            controller : 'createEditDebitController',
            templateUrl: 'mobile/create_edit_debit.html'
        };
        var autoDebitEditState = {
            name: "createEditAutoDebit",
            controller : 'createEditAutoDebitController',
            templateUrl: 'mobile/create_edit_auto_debit.html'
        };

        $stateProvider.state(loginState);
        $stateProvider.state(homeState);
        $stateProvider.state(registerState);
        $stateProvider.state(forgotPasswordState);
        $stateProvider.state(budgetState);
        $stateProvider.state(debitState);
        $stateProvider.state(autoDebitState);
        $stateProvider.state(statisticsState);
        $stateProvider.state(budgetEditState);
        $stateProvider.state(debitEditState);
        $stateProvider.state(searchState);
        $stateProvider.state(autoDebitEditState);

        $httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';
    })

    .controller("indexController", function ($state, $scope){
        if($scope.authenticated){
            $scope.fromLogin = true;
            $state.go("home");
        }
        else{
            $state.go("login");
        }
    });



