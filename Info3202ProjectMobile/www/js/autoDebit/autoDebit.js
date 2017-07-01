/**
 * Created by Hanzi Jing on 7/05/2017.
 */
angular.module('mainApp')
    .controller("autoDebitController",['$scope', '$rootScope', '$http', '$state', '$translate', 'dialogs', 'BASE_CONSTS',
        function($scope, $rootScope, $http, $state, $translate, dialogs, BASE_CONSTS){
        var view = $scope;
        view.lang = 'en-US';
        view.language = 'English';
        view.allBudgets = [];
        var loadBudget = function(){
            $http.get(BASE_CONSTS.ip + 'budget')
                .then(
                    function (response) {
                        view.allBudgets = response.data;
                    },
                    function (response) {
                        if(response!= null && response.data != null){
                            if(response.data.session != null){
                                dialogs.error("ERROR",  response.data.session, {size:'sm'});
                                $state.go("login");
                            }
                            else if(response.data.general != null){
                                dialogs.error("ERROR",  response.data.general, {size:'sm'});
                            }
                        }
                        else{
                            dialogs.error("ERROR", "System Error", {size:'sm'});
                        }
                    })
        };
        loadBudget();
        view.allAutoDebits = [];
        var loadAutoDebit = function(){
            var budgets = [];
            $rootScope.selectedAutoDebit = null;
            if($rootScope.selectedBudgets != null){
                budgets = $rootScope.selectedBudgets;
            }
            $http.post(BASE_CONSTS.ip + 'autoDebitGet', budgets)
                .then(
                    function (response) {
                        view.autoDebitGridOptions.data =  response.data;
                    },
                    function (response) {
                        if(response!= null && response.data != null){
                            if(response.data.session != null){
                                dialogs.error("ERROR",  response.data.session, {size:'sm'});
                                $state.go("login");
                            }
                            else if(response.data.general != null){
                                dialogs.error("ERROR",  response.data.general, {size:'sm'});
                            }
                        }
                        else{
                            dialogs.error("ERROR", "System Error", {size:'sm'});
                        }
                    });
        };

        view.autoDebitGridOptions = {
            columnDefs: [{name: 'categoryName'},
                {name: 'description'},
                {name: 'amount'},
                {name: 'debitInterval'}],
            multiSelect: false,
            modifierKeysToMultiSelect: false,
            enableRowSelection : true,
            enableRowHeaderSelection: false,
            enableHorizontalScrollbar: 1,
            onRegisterApi: function(gridApi){
                //set gridApi on scope
                view.gridApi = gridApi;
            },
            data: []
        };
        view.createAutoDebit = function(){
            $rootScope.selectedAutoDebit = null;
            $state.go('createEditAutoDebit');
        };

        view.editAutoDebit = function(){
            var selected =  view.gridApi.selection.getSelectedRows();
            if(selected != null && selected.length > 0) {
                $rootScope.selectedAutoDebit = selected[0];
                $state.go('createEditAutoDebit');
            } else {
                dialogs.notify("MESSAGE", "No Entry selected!", {size:'sm'});
            }
        };

        view.deleteAutoDebit = function(){
            $rootScope.selectedAutoDebit = null;
            var selected = view.gridApi.selection.getSelectedRows();
            if(selected != null && selected.length > 0) {
                if(selected[0].id != null){
                    dialogs.confirm("WARNING", "Auto-pay: " + selected.categoryName + " will be deleted", {size :'sm'})
                        .result.then(function(){
                            $http.delete(BASE_CONSTS.ip + 'autoDebit', {params: {id: selected[0].id}})
                                .then(
                                function () {
                                    $rootScope.$broadcast("refresh_statistics");
                                    loadAutoDebit();
                                },
                                function (response) {
                                    if(response!= null && response.data != null){
                                        if(response.data.delete != null){
                                            dialogs.error("ERROR",  response.data.delete, {size:'sm'});
                                        }
                                        else if(response.data.session != null){
                                            dialogs.error("ERROR",  response.data.session, {size:'sm'});
                                            $state.go("login");
                                        }
                                        else if(response.data.general != null){
                                            dialogs.error("ERROR",  response.data.general, {size:'sm'});
                                        }
                                    }
                                    else{
                                        dialogs.error("ERROR", "System Error", {size:'sm'});
                                    }
                                })
                    },function(b){
                    });
                }
            } else {
                dialogs.notify("MESSAGE", "No Entry selected!", {size:'sm'});
            }
        };
        loadAutoDebit();
        view.$on("reload_auto_debit", function (){
            loadAutoDebit();
        });
    }]);
