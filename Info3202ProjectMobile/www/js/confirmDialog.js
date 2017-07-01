/**
 * Created by shaun.jing on 8/05/2017.
 */
angular.module('mainApp')
    .controller("confirmDialogController", ['$scope',  'item', 'dialog', function($scope, item, dialog){
            $scope.item = item;

            $scope.save = function() {
                dialog.close($scope.item);
            };

            $scope.close = function(){
                dialog.close(undefined);
            };
    }]);