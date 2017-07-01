angular.module('mainApp')
    .controller("debitController",['$scope', '$rootScope', '$http', '$state', '$translate', 'dialogs', 'BASE_CONSTS',
        function($scope, $rootScope, $http, $state, $translate, dialogs, BASE_CONSTS){
        var view = $scope;
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
        view.allDebits = [];
        var loadDebit = function(){
            var budgets = [];
            $rootScope.selectedDebit = null;
            if($rootScope.selectedBudgets != null){
                budgets = $rootScope.selectedBudgets;
            }
            $http.post(BASE_CONSTS.ip + 'debitAll', budgets)
                .then(
                    function (response) {
                        view.mainGridOptions.data =  response.data;
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
        view.mainGridOptions = {
            columnDefs: [{name: 'categoryName'},
                {name: 'description'},
                {name: 'amount'},
                {name: 'date'}],
            paginationPageSizes: [15, 30, 45],
            paginationPageSize: 15,
            multiSelect: false,
            modifierKeysToMultiSelect: false,
            enableRowSelection : true,
            enableRowHeaderSelection: false,
            enableHorizontalScrollbar: 1,
            onRegisterApi: function(gridApi){
                view.gridApi = gridApi;
            },
            data: []
        };
        view.createDebit = function(){
            $rootScope.selectedDebit = null;
            $state.go('createEditDebit');
        };
        view.editDebit = function(){
            var selected =  view.gridApi.selection.getSelectedRows();
            if(selected != null && selected.length > 0) {
                $rootScope.selectedDebit = selected[0];
                $state.go('createEditDebit');
            } else {
                dialogs.notify("MESSAGE", "No Entry selected!", {size:'sm'});
            }
        };
        view.deleteDebit = function(){
            $rootScope.selectedDebit = null;
            var selected = view.gridApi.selection.getSelectedRows();
            if(selected != null && selected.length > 0) {
                if(selected[0].id != null){
                    dialogs.confirm("WARNING", "Auto-pay: " + selected.categoryName + " will be deleted", {size :'sm'})
                        .result.then(function(){
                            $http.delete(BASE_CONSTS.ip + 'debit', {params: {id: selected[0].id}})
                            .then(
                                function () {
                                    $rootScope.$broadcast("refresh_statistics");
                                    loadDebit();
                                },
                                function (response) {
                                    if(response!= null && response.data != null){
                                        if(response.data.delete != null){
                                            dialogs.error("ERROR",  response.data.delete, {size:'sm'});
                                        }
                                        else if(response.data.general != null){
                                            dialogs.error("ERROR",  response.data.general, {size:'sm'});
                                        }
                                        else if(response.data.session != null){
                                            dialogs.error("ERROR",  response.data.session, {size:'sm'});
                                            $state.go("login");
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
        loadDebit();
        view.$on("reload_debit", function (){
            loadDebit();
        });
    }
    ]);