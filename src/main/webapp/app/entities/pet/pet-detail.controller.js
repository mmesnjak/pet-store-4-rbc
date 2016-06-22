(function() {
    'use strict';

    angular
        .module('petStoreApp')
        .controller('PetDetailController', PetDetailController);

    PetDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Pet', 'Tag', 'Category'];

    function PetDetailController($scope, $rootScope, $stateParams, entity, Pet, Tag, Category) {
        var vm = this;

        vm.pet = entity;

        var unsubscribe = $rootScope.$on('petStoreApp:petUpdate', function(event, result) {
            vm.pet = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
