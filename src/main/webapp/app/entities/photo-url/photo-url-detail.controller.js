(function() {
    'use strict';

    angular
        .module('petStoreApp')
        .controller('PhotoUrlDetailController', PhotoUrlDetailController);

    PhotoUrlDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'PhotoUrl', 'Pet'];

    function PhotoUrlDetailController($scope, $rootScope, $stateParams, entity, PhotoUrl, Pet) {
        var vm = this;

        vm.photoUrl = entity;

        var unsubscribe = $rootScope.$on('petStoreApp:photoUrlUpdate', function(event, result) {
            vm.photoUrl = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
