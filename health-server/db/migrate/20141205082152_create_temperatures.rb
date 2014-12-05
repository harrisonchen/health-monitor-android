class CreateTemperatures < ActiveRecord::Migration
  def change
    create_table :temperatures do |t|
      t.decimal :fahrenheit
      t.decimal :celsius

      t.timestamps
    end
  end
end
