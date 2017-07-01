/**
 * Created by Hanzi Jing on 7/05/2017.
 */
angular.module('mainApp')
.controller('homeController', [ '$rootScope', '$state', function($rootScope, $state){
    $rootScope.selectedBudgets = [];
    $rootScope.selectedStatGraph = "PIE";
    $rootScope.selectedStatInterval = 'WEEK';
    if($rootScope.fromLogin){
        $rootScope.fromLogin = false;
        $state.go("home.budget");
    }
    $('nav a').click(function(e) {
        e.preventDefault();
        $('nav a').removeClass('active');
        $(this).addClass('active');
    });
}]);