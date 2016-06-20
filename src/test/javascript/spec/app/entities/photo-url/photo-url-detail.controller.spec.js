'use strict';

describe('Controller Tests', function() {

    describe('PhotoUrl Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPhotoUrl, MockPet;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPhotoUrl = jasmine.createSpy('MockPhotoUrl');
            MockPet = jasmine.createSpy('MockPet');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'PhotoUrl': MockPhotoUrl,
                'Pet': MockPet
            };
            createController = function() {
                $injector.get('$controller')("PhotoUrlDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'petStoreApp:photoUrlUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
