class CreateHeartbeats < ActiveRecord::Migration
  def change
    create_table :heartbeats do |t|
      t.integer :beats_per_second, default: 0
      t.integer :beats_per_minute, default: 0

      t.timestamps
    end
  end
end
