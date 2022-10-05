After you complete this tutorial, you can free resources by stopping the
tutorial Docker containers and removing images with the following
command:

.. code-block:: bash
   :copyable: true

   docker-compose -p mongo-kafka down --rmi 'all'

To restart the containers, follow the same steps required to start them
in the :ref:`Tutorial Setup <tutorial-setup-run-environment>`.
