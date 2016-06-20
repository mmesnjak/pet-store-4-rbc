(function() {
    'use strict';

    angular
        .module('petStoreApp')
        .controller('TagDetailController', TagDetailController);

    TagDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Tag', 'Pet'];

    function TagDetailController($scope, $rootScope, $stateParams, entity, Tag, Pet) {
        var vm = this;

        vm.tag = entity;

        var unsubscribe = $rootScope.$on('petStoreApp:tagUpdate', function(event, result) {
            vm.tag = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
