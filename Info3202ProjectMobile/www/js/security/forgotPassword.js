/**
 * Created by Hanzi Jing on 7/05/2017.
 */
angular.module('mainApp')
    .controller('forgotPasswordController',[ '$scope', '$http', 'BASE_CONSTS', function($scope, $http, BASE_CONSTS){
        var view = $scope;
        view.error = false;
        view.success = false;
        view.resetPassword = function(){
            view.error = false;
            view.success = false;
            view.email_error ="";
            view.email_message = "";
            $http.post(BASE_CONSTS.ip + 'resetPassword', view.emailAddr)
                .then(
                    function () {
                        view.success = true;
                        view.email_message = "The email for resetting password was set to your account!";
                    },
                    function () {
                        view.error = true;
                        view.email_error = "Error In E-mail Address";
                    });
        }
    }]);